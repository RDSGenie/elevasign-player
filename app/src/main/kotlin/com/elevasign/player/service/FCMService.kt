package com.elevasign.player.service

import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.elevasign.player.worker.SyncWorker
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Receives FCM push notifications from the admin panel.
 * A push message wakes up the player and triggers an immediate sync.
 * The message payload can optionally contain a "force_sync" action.
 */
class FCMService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FCMService"
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.i(TAG, "FCM message received: ${message.data}")

        // Any FCM message triggers an immediate sync
        enqueueSyncNow()
    }

    override fun onNewToken(token: String) {
        Log.i(TAG, "FCM token refreshed: ${token.take(20)}...")
        // TODO: If you want to update the FCM token in the backend, call player-register
        // or add a dedicated endpoint. For now, the token is sent on next pairing.
    }

    private fun enqueueSyncNow() {
        val request = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueue(request)

        Log.d(TAG, "Immediate sync enqueued via FCM wake-up")
    }
}
