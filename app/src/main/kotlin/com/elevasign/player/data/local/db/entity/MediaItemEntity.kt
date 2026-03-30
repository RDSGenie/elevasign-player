package com.elevasign.player.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_items")
data class MediaItemEntity(
    @PrimaryKey
    @ColumnInfo(name = "media_id") val mediaId: String,
    @ColumnInfo(name = "file_url") val fileUrl: String?,
    @ColumnInfo(name = "file_type") val fileType: String,
    @ColumnInfo(name = "display_duration_seconds") val displayDurationSeconds: Int,
    @ColumnInfo(name = "sort_order") val sortOrder: Int,
    /** Absolute path of locally cached file, null if not downloaded yet */
    @ColumnInfo(name = "local_path") val localPath: String?,
    @ColumnInfo(name = "playlist_id") val playlistId: String,
    @ColumnInfo(name = "playlist_name") val playlistName: String,
    @ColumnInfo(name = "zone_name", defaultValue = "main") val zoneName: String = "main",
)
