package com.elevasign.player.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("pairing_code") val pairingCode: String,
    @SerializedName("device_id") val deviceId: String,
    @SerializedName("fcm_token") val fcmToken: String? = null,
    @SerializedName("app_version") val appVersion: String? = null,
    @SerializedName("os_version") val osVersion: String? = null,
    @SerializedName("screen_resolution") val screenResolution: String? = null,
)
