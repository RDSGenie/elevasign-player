package com.elevasign.player.domain.usecase

import android.util.Log
import com.elevasign.player.data.local.datastore.PlayerPreferences
import com.elevasign.player.data.repository.PlayerRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SyncManifestUseCase @Inject constructor(
    private val repository: PlayerRepository,
    private val prefs: PlayerPreferences,
) {
    companion object {
        private const val TAG = "SyncManifestUseCase"
    }

    /**
     * Fetches the remote manifest and persists it locally.
     * Skips download if content_version and manifest_hash are unchanged.
     * @return true if content was updated, false if nothing changed or error
     */
    suspend operator fun invoke(): Boolean {
        val screenId = prefs.screenId.first() ?: run {
            Log.w(TAG, "No screen_id stored, skipping sync")
            return false
        }

        val syncResult = repository.sync(screenId)
        if (syncResult.isFailure) {
            Log.e(TAG, "Sync failed: ${syncResult.exceptionOrNull()?.message}")
            return false
        }

        val remote = syncResult.getOrThrow()
        val localVersion = prefs.contentVersion.first()
        val localHash = prefs.manifestHash.first()

        if (remote.contentVersion <= localVersion && remote.manifestHash == localHash) {
            Log.d(TAG, "Content unchanged (version=$localVersion), skipping persist")
            return false
        }

        Log.i(TAG, "New content detected: remote v${remote.contentVersion} vs local v$localVersion")
        repository.persistSyncResponse(remote)
        return true
    }
}
