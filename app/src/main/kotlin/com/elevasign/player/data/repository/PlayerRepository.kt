package com.elevasign.player.data.repository

import android.util.Log
import com.elevasign.player.data.local.datastore.PlayerPreferences
import com.elevasign.player.data.local.db.dao.AnnouncementDao
import com.elevasign.player.data.local.db.dao.LayoutZoneDao
import com.elevasign.player.data.local.db.dao.MediaItemDao
import com.elevasign.player.data.local.db.entity.AnnouncementEntity
import com.elevasign.player.data.local.db.entity.LayoutZoneEntity
import com.elevasign.player.data.local.db.entity.MediaItemEntity
import com.elevasign.player.data.local.file.MediaFileManager
import com.elevasign.player.data.remote.SupabaseApi
import com.elevasign.player.data.remote.download.MediaDownloader
import com.elevasign.player.data.remote.dto.AnnouncementDto
import com.elevasign.player.data.remote.dto.CommandResultRequest
import com.elevasign.player.data.remote.dto.HeartbeatRequest
import com.elevasign.player.data.remote.dto.HeartbeatResponse
import com.elevasign.player.data.remote.dto.LogPlayRequest
import com.elevasign.player.data.remote.dto.RegisterRequest
import com.elevasign.player.data.remote.dto.RegisterResponse
import com.elevasign.player.data.remote.dto.SyncResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepository @Inject constructor(
    private val api: SupabaseApi,
    private val mediaItemDao: MediaItemDao,
    private val announcementDao: AnnouncementDao,
    private val layoutZoneDao: LayoutZoneDao,
    private val prefs: PlayerPreferences,
    private val downloader: MediaDownloader,
    private val fileManager: MediaFileManager,
) {
    companion object {
        private const val TAG = "PlayerRepository"
    }

    suspend fun register(request: RegisterRequest): Result<RegisterResponse> =
        withContext(Dispatchers.IO) {
            runCatching { api.register(request) }
        }

    suspend fun sync(screenId: String): Result<SyncResponse> =
        withContext(Dispatchers.IO) {
            runCatching { api.sync(screenId) }
        }

    suspend fun heartbeat(request: HeartbeatRequest): Result<HeartbeatResponse> =
        withContext(Dispatchers.IO) {
            runCatching { api.heartbeat(request) }
        }

    suspend fun reportCommandResult(commandId: String, success: Boolean, error: String? = null) =
        withContext(Dispatchers.IO) {
            runCatching {
                val resultMap = if (error != null) mapOf("error" to error) else null
                api.commandResult(
                    CommandResultRequest(
                        commandId = commandId,
                        status = if (success) "completed" else "failed",
                        result = resultMap,
                    )
                )
            }
        }

    /**
     * Persists a sync response to Room and downloads missing media files.
     * Only downloads files not already cached locally.
     */
    suspend fun persistSyncResponse(response: SyncResponse) = withContext(Dispatchers.IO) {
        val playlist = response.playlist ?: return@withContext

        // Build entities
        val mediaEntities = playlist.items.map { item ->
            val existingLocal = fileManager.localFile(item.mediaId, item.fileType)
            MediaItemEntity(
                mediaId = item.mediaId,
                fileUrl = item.fileUrl,
                fileType = item.fileType,
                displayDurationSeconds = item.displayDuration,
                sortOrder = item.sortOrder,
                localPath = if (existingLocal.exists()) existingLocal.absolutePath else null,
                playlistId = playlist.id,
                playlistName = playlist.name,
            )
        }

        val announcementEntities = response.announcements.map { ann ->
            AnnouncementEntity(
                id = ann.id,
                title = ann.title,
                body = ann.body,
                displayType = ann.displayType,
                bgColor = ann.bgColor,
                textColor = ann.textColor,
                priority = ann.priority,
                startsAt = ann.startsAt,
                expiresAt = ann.expiresAt,
                isActive = ann.isActive,
            )
        }

        val zoneEntities = response.layoutZones.map { zone ->
            LayoutZoneEntity(
                id = zone.id,
                zoneName = zone.zoneName,
                zoneType = zone.zoneType,
                playlistId = zone.playlistId,
                positionXPercent = zone.positionXPercent,
                positionYPercent = zone.positionYPercent,
                widthPercent = zone.widthPercent,
                heightPercent = zone.heightPercent,
                zIndex = zone.zIndex,
            )
        }

        // Persist to Room
        val keepIds = mediaEntities.map { it.mediaId }.toSet()
        mediaItemDao.deleteNotIn(keepIds.toList().ifEmpty { listOf("__none__") })
        mediaItemDao.upsertAll(mediaEntities)
        announcementDao.deleteAll()
        announcementDao.upsertAll(announcementEntities)
        layoutZoneDao.deleteAll()
        layoutZoneDao.upsertAll(zoneEntities)

        // Update sync state
        prefs.updateSyncState(response.contentVersion, response.manifestHash)
        prefs.updateCurrentPlaylist(playlist.id, playlist.name)

        // Evict old files before downloading
        fileManager.evictIfNeeded(keepIds)

        // Download missing files
        for (item in playlist.items) {
            val file = fileManager.localFile(item.mediaId, item.fileType)
            if (!file.exists() && item.fileUrl != null) {
                try {
                    val downloaded = downloader.download(item.fileUrl, item.mediaId, item.fileType)
                    mediaItemDao.updateLocalPath(item.mediaId, downloaded.absolutePath)
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to download ${item.mediaId}: ${e.message}")
                }
            }
        }
    }

    /**
     * Persist announcements independently of content version.
     * Announcements can change without bumping content_version.
     */
    suspend fun persistAnnouncements(announcements: List<AnnouncementDto>) = withContext(Dispatchers.IO) {
        val entities = announcements.map { ann ->
            AnnouncementEntity(
                id = ann.id,
                title = ann.title,
                body = ann.body,
                displayType = ann.displayType,
                bgColor = ann.bgColor,
                textColor = ann.textColor,
                priority = ann.priority,
                startsAt = ann.startsAt,
                expiresAt = ann.expiresAt,
                isActive = ann.isActive,
            )
        }
        announcementDao.deleteAll()
        announcementDao.upsertAll(entities)
    }

    fun observeMediaItems() = mediaItemDao.observeAll()

    suspend fun getContentVersion() = prefs.contentVersion.first()
    suspend fun getManifestHash() = prefs.manifestHash.first()
    suspend fun getScreenId() = prefs.screenId.first()

    suspend fun logPlay(request: LogPlayRequest) = withContext(Dispatchers.IO) {
        api.logPlay(request)
    }
}
