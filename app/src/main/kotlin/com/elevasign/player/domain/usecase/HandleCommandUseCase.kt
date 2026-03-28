package com.elevasign.player.domain.usecase

import android.content.Context
import android.media.AudioManager
import android.os.PowerManager
import android.util.Log
import com.elevasign.player.data.local.datastore.PlayerPreferences
import com.elevasign.player.data.local.file.MediaFileManager
import com.elevasign.player.data.repository.PlayerRepository
import com.elevasign.player.domain.model.CommandType
import com.elevasign.player.domain.model.DeviceCommand
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class HandleCommandUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: PlayerRepository,
    private val prefs: PlayerPreferences,
    private val fileManager: MediaFileManager,
    private val syncManifest: SyncManifestUseCase,
) {
    companion object {
        private const val TAG = "HandleCommandUseCase"
    }

    suspend operator fun invoke(command: DeviceCommand) {
        Log.i(TAG, "Executing command: ${command.type} (id=${command.id})")
        var success = true
        var error: String? = null

        try {
            when (command.type) {
                CommandType.RESTART_APP -> {
                    repository.reportCommandResult(command.id, true)
                    // Small delay so result is reported before kill
                    delay(500)
                    android.os.Process.killProcess(android.os.Process.myPid())
                }

                CommandType.REBOOT_DEVICE -> {
                    repository.reportCommandResult(command.id, true)
                    delay(500)
                    val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                    pm.reboot(null)
                }

                CommandType.FORCE_SYNC -> {
                    syncManifest()
                }

                CommandType.CLEAR_CACHE -> {
                    fileManager.clearAll()
                    prefs.resetSyncState()
                    // Trigger re-sync after clear
                    CoroutineScope(Dispatchers.IO).launch { syncManifest() }
                }

                CommandType.SET_VOLUME -> {
                    val level = (command.payload?.get("level") as? Double)?.toInt()
                        ?: (command.payload?.get("level") as? Int)
                    if (level != null) {
                        val audio = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                        val maxVol = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                        val targetVol = (maxVol * level / 100.0).toInt().coerceIn(0, maxVol)
                        audio.setStreamVolume(AudioManager.STREAM_MUSIC, targetVol, 0)
                        Log.i(TAG, "Volume set to $level% ($targetVol/$maxVol)")
                    } else {
                        error = "Missing 'level' in payload"
                        success = false
                    }
                }

                CommandType.TAKE_SCREENSHOT -> {
                    // Screenshot requires PixelCopy + running Activity context.
                    // The PlayerViewModel observes commands and handles this in the UI.
                    // We just report that we received the command; actual screenshot is
                    // triggered via a SharedFlow in the ViewModel.
                    Log.d(TAG, "Screenshot command delegated to PlayerViewModel")
                    // Don't report result here – PlayerViewModel will call reportCommandResult
                    return
                }

                CommandType.UNKNOWN -> {
                    Log.w(TAG, "Unknown command type ignored: ${command.id}")
                    error = "Unknown command type"
                    success = false
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Command execution failed: ${e.message}")
            success = false
            error = e.message
        }

        if (command.type != CommandType.RESTART_APP && command.type != CommandType.REBOOT_DEVICE) {
            repository.reportCommandResult(command.id, success, error)
        }
    }
}
