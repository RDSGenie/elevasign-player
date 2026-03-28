package com.elevasign.player.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("screen_id") val screenId: String,
    @SerializedName("name") val name: String,
    @SerializedName("layout_template") val layoutTemplate: String,
    @SerializedName("orientation") val orientation: String,
)
