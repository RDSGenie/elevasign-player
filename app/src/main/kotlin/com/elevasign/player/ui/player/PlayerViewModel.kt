package com.elevasign.player.ui.player

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elevasign.player.data.local.datastore.PlayerPreferences
import com.elevasign.player.data.local.db.dao.AnnouncementDao
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class PlayerUiState(
    val currentItem: PlaybackItem? = null,
    val playlistSize: Int = 0,
    val currentIndex: Int = 0,
    val announcements: List<ActiveAnnouncement> = emptyList(),
    val isLoading: Boolean = true,
    val isEmpty: Boolean = false,
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: PlayerRepository,
    private val announcementDao: AnnouncementDao,
    private val prefs: PlayerPreferences,
) : ViewModel() {

    companion object {
        private const val TAG = "PlayerViewModel"
    }

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    // Emits command IDs for screenshot — handled by PlayerScreen
    private val _screenshotCommand = MutableSharedFlow<String>()
    val screenshotCommand: SharedFlow<String> = _screenshotCommand.asSharedFlow()

    private var playlist: List<PlaybackItem> = emptyList()
    private var currentIndex = 0
    private var advanceJob: kotlinx.coroutines.Job? = null

    init {
        observePlaylist()
        observeAnnouncements()
    }

    private fun observePlaylist() {
        viewModelScope.launch {
            repository.observeMediaItems().collectLatest { entities ->
                playlist = entities.toPlaybackItems()
                if (playlist.isEmpty()) {
                    _uiState.value = PlayerUiState(isLoading = false, isEmpty = true)
                    advanceJob?.cancel()
                } else {
                    currentIndex = currentIndex.coerceIn(0, playlist.size - 1)
                    updateCurrentItem()
                    // Don't restart timer when playlist refreshes mid-playback
                    if (advanceJob?.isActive != true) startAdvanceLoop()
                }
            }
        }
    }

    private fun observeAnnouncements() {
        viewModelScope.launch {
            announcementDao.observeActive(Instant.now().toString()).collectLatest { entities ->
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
        _uiState.value = _uiState.value.copy(
            currentItem = item,
            playlistSize = playlist.size,
            currentIndex = currentIndex,
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
