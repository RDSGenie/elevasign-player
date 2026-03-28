package com.elevasign.player.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SyncResponse(
    @SerializedName("playlist") val playlist: PlaylistDto?,
    @SerializedName("announcements") val announcements: List<AnnouncementDto> = emptyList(),
    @SerializedName("layout_zones") val layoutZones: List<LayoutZoneDto> = emptyList(),
    @SerializedName("manifest_hash") val manifestHash: String,
    @SerializedName("content_version") val contentVersion: Long,
)

data class PlaylistDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("items") val items: List<PlaylistItemDto> = emptyList(),
)

data class PlaylistItemDto(
    @SerializedName("media_id") val mediaId: String,
    @SerializedName("file_url") val fileUrl: String?,
    @SerializedName("file_type") val fileType: String,
    @SerializedName("display_duration") val displayDuration: Int,
    @SerializedName("sort_order") val sortOrder: Int,
)

data class AnnouncementDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String,
    @SerializedName("display_type") val displayType: String,
    @SerializedName("bg_color") val bgColor: String,
    @SerializedName("text_color") val textColor: String,
    @SerializedName("priority") val priority: Int,
    @SerializedName("target_screens") val targetScreens: List<String>?,
    @SerializedName("starts_at") val startsAt: String,
    @SerializedName("expires_at") val expiresAt: String?,
    @SerializedName("is_active") val isActive: Boolean,
)

data class LayoutZoneDto(
    @SerializedName("id") val id: String,
    @SerializedName("zone_name") val zoneName: String,
    @SerializedName("zone_type") val zoneType: String,
    @SerializedName("playlist_id") val playlistId: String?,
    @SerializedName("widget_config") val widgetConfig: Map<String, Any>?,
    @SerializedName("position_x_percent") val positionXPercent: Float,
    @SerializedName("position_y_percent") val positionYPercent: Float,
    @SerializedName("width_percent") val widthPercent: Float,
    @SerializedName("height_percent") val heightPercent: Float,
    @SerializedName("z_index") val zIndex: Int,
)
