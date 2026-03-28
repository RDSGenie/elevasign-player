package com.elevasign.player.data.remote.dto

import com.google.gson.annotations.SerializedName

data class HeartbeatRequest(
    @SerializedName("screen_id") val screenId: String,
    @SerializedName("wifi_signal_dbm") val wifiSignalDbm: Int? = null,
    @SerializedName("free_storage_mb") val freeStorageMb: Long? = null,
    @SerializedName("total_storage_mb") val totalStorageMb: Long? = null,
    @SerializedName("current_playlist") val currentPlaylist: String? = null,
    @SerializedName("current_media_item") val currentMediaItem: String? = null,
    @SerializedName("uptime_seconds") val uptimeSeconds: Long? = null,
    @SerializedName("app_version") val appVersion: String? = null,
)
