package com.elevasign.player.data.remote.dto

import com.google.gson.annotations.SerializedName

data class HeartbeatResponse(
    @SerializedName("commands") val commands: List<CommandDto> = emptyList(),
)

data class CommandDto(
    @SerializedName("id") val id: String,
    @SerializedName("screen_id") val screenId: String,
    @SerializedName("command_type") val commandType: String,
    @SerializedName("payload") val payload: Map<String, Any>?,
    @SerializedName("status") val status: String,
    @SerializedName("created_at") val createdAt: String,
)
