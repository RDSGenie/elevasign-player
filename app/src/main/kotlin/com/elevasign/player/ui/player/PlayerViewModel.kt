package com.elevasign.player.ui.player

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elevasign.player.data.local.datastore.PlayerPreferences
import com.elevasign.player.data.local.db.dao.AnnouncementDao
import com.elevasign.player.data.local.db.dao.LayoutZoneDao
import com.elevasign.player.data.local.db.entity.LayoutZoneEntity
import com.elevasign.player.data.local.db.entity.MediaItemEntity
import com.elevasign.player.data.remote.dto.LogPlayRequest
import com.elevasign.player.data.repository.PlayerRepository
import com.elevasign.player.domain.model.ActiveAnnouncement
import com.elevasign.player.domain.model.PlaybackItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class ZonePlaybackState(
    val zoneName: String,
    val currentItem: PlaybackItem? = null,
    val playbackGeneration: Long = 0,
    val positionXPercent: Float = 0f,
    val positionYPercent: Float = 0f,
    val widthPercent: Float = 100f,
    val heightPercent: Float = 100f,
)

data class PlayerUiState(
    val currentItem: PlaybackItem? = null,
    val playlistSize: Int = 0,
    val currentIndex: Int = 0,
    val playbackGeneration: Long = 0,
    val zones: List<ZonePlaybackState> = emptyList(), // for multi-zone layouts
    val isMultiZone: Boolean = false,
    val announcements: List<ActiveAnnouncement> = emptyList(),
    val isLoading: Boolean = true,
    val isEmpty: Boolean = false,
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: PlayerRepository,
    private val announcementDao: AnnouncementDao,
    private val layoutZoneDao: LayoutZoneDao,
    private val prefs: PlayerPreferences,
) : ViewModel() {

    companion object {
        private const val TAG = "PlayerViewModel"
    }

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    private val _screenshotCommand = MutableSharedFlow<String>()
    val screenshotCommand: SharedFlow<String> = _screenshotCommand.asSharedFlow()

    // Single-zone playback state
    private var playlist: List<PlaybackItem> = emptyList()
    private var currentIndex = 0
    private var playbackGeneration = 0L
    private var advanceJob: kotlinx.coroutines.Job? = null

    // Multi-zone playback state
    private val zonePlaybackData = mutableMapOf<String, MutableList<PlaybackItem>>()
    private val zoneCurrentIndex = mutableMapOf<String, Int>()
    private val zoneGeneration = mutableMapOf<String, Long>()
    private val zoneAdvanceJobs = mutableMapOf<String, kotlinx.coroutines.Job>()

    init {
        observePlaylist()
        observeAnnouncements()
    }

    private fun observePlaylist() {
        viewModelScope.launch {
            repository.observeMediaItems().collectLatest { entities ->
                // Check if we have multi-zone content
                val zones = try { layoutZoneDao.getAll() } catch (_: Exception) { emptyList() }
                val hasMultiZone = zones.size > 1

                if (hasMultiZone) {
                    setupMultiZonePlayback(entities, zones)
                } else {
                    // Single zone (default)
                    playlist = entities.filter { it.zoneName == "main" || zones.isEmpty() }.toPlaybackItems()
                    if (playlist.isEmpty()) {
                        // Try all items regardless of zone
                        playlist = entities.toPlaybackItems()
                    }
                    if (playlist.isEmpty()) {
                        _uiState.value = _uiState.value.copy(isLoading = false, isEmpty = true, isMultiZone = false, zones = emptyList())
                        advanceJob?.cancel()
                    } else {
                        currentIndex = currentIndex.coerceIn(0, playlist.size - 1)
                        updateCurrentItem()
                        if (advanceJob?.isActive != true) startAdvanceLoop()
                    }
                }
            }
        }
    }

    private fun setupMultiZonePlayback(entities: List<MediaItemEntity>, zones: List<LayoutZoneEntity>) {
        // Group media by zone
        val byZone = entities.groupBy { it.zoneName }

        // Cancel all existing zone jobs
        zoneAdvanceJobs.values.forEach { it.cancel() }
        zoneAdvanceJobs.clear()

        val zoneStates = mutableListOf<ZonePlaybackState>()
        for (zone in zones) {
            val zoneItems = (byZone[zone.zoneName] ?: emptyList()).toPlaybackItems()
            zonePlaybackData[zone.zoneName] = zoneItems.toMutableList()
            if (!zoneCurrentIndex.containsKey(zone.zoneName)) zoneCurrentIndex[zone.zoneName] = 0
            if (!zoneGeneration.containsKey(zone.zoneName)) zoneGeneration[zone.zoneName] = 0L

            val idx = (zoneCurrentIndex[zone.zoneName] ?: 0).coerceIn(0, (zoneItems.size - 1).coerceAtLeast(0))
            zoneCurrentIndex[zone.zoneName] = idx
            zoneGeneration[zone.zoneName] = (zoneGeneration[zone.zoneName] ?: 0) + 1

            zoneStates.add(
                ZonePlaybackState(
                    zoneName = zone.zoneName,
                    currentItem = zoneItems.getOrNull(idx),
                    playbackGeneration = zoneGeneration[zone.zoneName] ?: 0,
                    positionXPercent = zone.positionXPercent,
                    positionYPercent = zone.positionYPercent,
                    widthPercent = zone.widthPercent,
                    heightPercent = zone.heightPercent,
                )
            )

            // Start independent advance loop for each zone
            if (zoneItems.isNotEmpty()) {
                startZoneAdvanceLoop(zone.zoneName, zone)
            }
        }

        _uiState.value = _uiState.value.copy(
            isMultiZone = true,
            zones = zoneStates,
            isLoading = false,
            isEmpty = zoneStates.all { it.currentItem == null },
        )
    }

    private fun startZoneAdvanceLoop(zoneName: String, zone: LayoutZoneEntity) {
        zoneAdvanceJobs[zoneName]?.cancel()
        zoneAdvanceJobs[zoneName] = viewModelScope.launch {
            while (true) {
                val items = zonePlaybackData[zoneName] ?: break
                val idx = zoneCurrentIndex[zoneName] ?: 0
                val item = items.getOrNull(idx) ?: break
                val durationMs = item.durationSeconds * 1000L
                delay(durationMs)
                // Advance to next item in this zone
                val nextIdx = (idx + 1) % items.size
                zoneCurrentIndex[zoneName] = nextIdx
                zoneGeneration[zoneName] = (zoneGeneration[zoneName] ?: 0) + 1
                val nextItem = items.getOrNull(nextIdx)
                // Update just this zone in the UI state
                _uiState.value = _uiState.value.copy(
                    zones = _uiState.value.zones.map { zs ->
                        if (zs.zoneName == zoneName) zs.copy(
                            currentItem = nextItem,
                            playbackGeneration = zoneGeneration[zoneName] ?: 0,
                        ) else zs
                    }
                )
            }
        }
    }

    private fun observeAnnouncements() {
        viewModelScope.launch {
            // Ticker refreshes the timestamp every 60s so announcements with
            // future starts_at become visible without requiring an app restart.
            val timestampTicker = flow {
                while (true) {
                    emit(Instant.now().toString())
                    delay(60_000)
                }
            }
            timestampTicker.flatMapLatest { now ->
                announcementDao.observeActive(now)
            }.collectLatest { entities ->
                val active = entities.map { entity ->
                    ActiveAnnouncement(
                        id = entity.id,
                        title = entity.title,
                        body = entity.body,
                        displayType = ActiveAnnouncement.fromString(entity.displayType),
                        bgColor = entity.bgColor,
                        textColor = entity.textColor,
                        priority = entity.priority,
                    )
                }
                _uiState.value = _uiState.value.copy(announcements = active)
            }
        }
    }

    private fun startAdvanceLoop() {
        advanceJob?.cancel()
        advanceJob = viewModelScope.launch {
            while (true) {
                val item = playlist.getOrNull(currentIndex) ?: break
                val durationMs = item.durationSeconds * 1000L
                val startedAt = Instant.now().toString()
                Log.d(TAG, "Playing item ${currentIndex + 1}/${playlist.size}: ${item.mediaId} for ${item.durationSeconds}s")
                delay(durationMs)
                logPlay(item, startedAt, durationMs, completed = true)
                advance()
            }
        }
    }

    fun advance() {
        if (playlist.isEmpty()) return
        currentIndex = (currentIndex + 1) % playlist.size
        updateCurrentItem()
        // Restart timer for next item
        startAdvanceLoop()
    }

    fun onVideoEnded() {
        // Video finished naturally — advance immediately, log as completed
        advanceJob?.cancel()
        val item = playlist.getOrNull(currentIndex)
        if (item != null) {
            val durationMs = item.durationSeconds * 1000L
            logPlay(item, Instant.now().toString(), durationMs, completed = true)
        }
        advance()
    }

    private fun logPlay(item: PlaybackItem, playedAt: String, durationMs: Long, completed: Boolean) {
        viewModelScope.launch {
            try {
                val screenId = prefs.screenId.first() ?: return@launch
                repository.logPlay(
                    LogPlayRequest(
                        screenId = screenId,
                        mediaItemId = item.mediaId,
                        playlistId = item.playlistId,
                        durationMs = durationMs,
                        completed = completed,
                        playedAt = playedAt,
                    )
                )
            } catch (e: Exception) {
                Log.w(TAG, "logPlay failed (non-critical): ${e.message}")
            }
        }
    }

    private fun updateCurrentItem() {
        val item = playlist.getOrNull(currentIndex)
        playbackGeneration++
        _uiState.value = _uiState.value.copy(
            currentItem = item,
            playlistSize = playlist.size,
            currentIndex = currentIndex,
            playbackGeneration = playbackGeneration,
            isLoading = false,
            isEmpty = item == null,
        )
    }

    fun emitScreenshotCommand(commandId: String) {
        viewModelScope.launch { _screenshotCommand.emit(commandId) }
    }

    private fun List<MediaItemEntity>.toPlaybackItems(): List<PlaybackItem> = map { entity ->
        PlaybackItem(
            mediaId = entity.mediaId,
            localPath = entity.localPath,
            fileUrl = entity.fileUrl,
            fileType = entity.fileType,
            durationSeconds = entity.displayDurationSeconds,
            sortOrder = entity.sortOrder,
            playlistId = entity.playlistId,
            playlistName = entity.playlistName,
        )
    }
}
