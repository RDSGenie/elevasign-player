package com.elevasign.player.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "announcements")
data class AnnouncementEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "body") val body: String,
    @ColumnInfo(name = "display_type") val displayType: String,
    @ColumnInfo(name = "bg_color") val bgColor: String,
    @ColumnInfo(name = "text_color") val textColor: String,
    @ColumnInfo(name = "priority") val priority: Int,
    @ColumnInfo(name = "starts_at") val startsAt: String,
    @ColumnInfo(name = "expires_at") val expiresAt: String?,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
)
