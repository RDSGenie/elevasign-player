package com.elevasign.player.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "layout_zones")
data class LayoutZoneEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "zone_name") val zoneName: String,
    @ColumnInfo(name = "zone_type") val zoneType: String,
    @ColumnInfo(name = "playlist_id") val playlistId: String?,
    @ColumnInfo(name = "position_x_percent") val positionXPercent: Float,
    @ColumnInfo(name = "position_y_percent") val positionYPercent: Float,
    @ColumnInfo(name = "width_percent") val widthPercent: Float,
    @ColumnInfo(name = "height_percent") val heightPercent: Float,
    @ColumnInfo(name = "z_index") val zIndex: Int,
)
