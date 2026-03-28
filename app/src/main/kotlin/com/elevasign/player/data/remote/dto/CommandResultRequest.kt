package com.elevasign.player.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CommandResultRequest(
    @SerializedName("command_id") val commandId: String,
    @SerializedName("status") val status: String,
    @SerializedName("result") val result: Map<String, Any>? = null,
)
