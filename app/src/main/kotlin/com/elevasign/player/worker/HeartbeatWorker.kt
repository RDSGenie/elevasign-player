package com.elevasign.player.worker

import android.content.Context
import android.net.wifi.WifiManager
import android.os.SystemClock
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.elevasign.player.BuildConfig
import com.elevasign.player.data.local.datastore.PlayerPreferences
import com.elevasign.player.data.local.file.MediaFileManager
import com.elevasign.player.data.remote.dto.HeartbeatRequest
import com.elevasign.player.data.repository.PlayerRepository
import com.elevasign.player.domain.model.DeviceCommand
import com.elevasign.player.domain.usecase.HandleCommandUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class HeartbeatWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: PlayerRepository,
    private val prefs: PlayerPreferences,
    private val fileManager: MediaFileManager,
    private val handleCommand: HandleCommandUseCase,
) : CoroutineWorker(context, params) {

    companion object {
        const val TAG = "HeartbeatWorker"
        const val WORK_NAME = "elevasign_periodic_heartbeat"
    }

    override suspend fun doWork(): Result {
        val screenId = prefs.screenId.first() ?: run {
            Log.w(TAG, "No screen_id, skipping heartbeat")
            return Result.success()
        }

        val wifiSignal = getWifiSignal()
        val (totalMb, freeMb) = fileManager.storageStats()
        val uptimeSeconds = SystemClock.elapsedRealtime() / 1000

        val request = HeartbeatRequest(
            screenId = screenId,
            wifiSignalDbm = wifiSignal,
            freeStorageMb = freeMb,
            totalStorageMb = totalMb,
            currentPlaylist = prefs.currentPlaylistId.first(),
            uptimeSeconds = uptimeSeconds,
            appVersion = BuildConfig.VERSION_NAME,
        )

        val result = repository.heartbeat(request)
        if (result.isFailure) {
            Log.w(TAG, "Heartbeat failed: ${result.exceptionOrNull()?.message}")
            return Result.success() // Don't retry heartbeat; next scheduled run will try
        }

        val response = result.getOrThrow()
        Log.d(TAG, "Heartbeat OK, ${response.commands.size} pending command(s)")

        // Execute each pending command
        for (cmdDto in response.commands) {
            val command = DeviceCommand(
                id = cmdDto.id,
                type = DeviceCommand.fromString(cmdDto.commandType),
                payload = cmdDto.payload,
            )
            handleCommand(command)
        }

        return Result.success()
    }

    private fun getWifiSignal(): Int? {
        return try {
            val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val info = wifiManager.connectionInfo
            if (info.rssi == -127) null else info.rssi
        } catch (e: Exception) {
            null
        }
    }
}
