package com.elevasign.player.receiver

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Device admin receiver for kiosk mode and reboot commands.
 * To enable device admin: adb shell dpm set-device-owner com.elevasign.player/.receiver.DeviceAdminReceiver
 * Or grant via Settings → Security → Device Admin apps
 */
class DeviceAdminReceiver : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        Log.i("DeviceAdminReceiver", "Device admin enabled")
    }

    override fun onDisabled(context: Context, intent: Intent) {
        Log.w("DeviceAdminReceiver", "Device admin disabled")
    }

    override fun onLockTaskModeEntering(context: Context, intent: Intent, pkg: String) {
        Log.i("DeviceAdminReceiver", "Lock task mode entered: $pkg")
    }

    override fun onLockTaskModeExiting(context: Context, intent: Intent) {
        Log.i("DeviceAdminReceiver", "Lock task mode exited")
    }
}
