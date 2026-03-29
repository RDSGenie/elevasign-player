package com.elevasign.player.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LogPlayRequest(
    @SerializedName("screen_id")    val screenId: String,
    @SerializedName("media_item_id") val mediaItemId: String?,
    @SerializedName("playlist_id")  val playlistId: String?,
    @SerializedName("duration_ms")  val durationMs: Long?,
    @SerializedName("completed")    val completed: Boolean,
    @SerializedName("played_at")    val playedAt: String,
)
