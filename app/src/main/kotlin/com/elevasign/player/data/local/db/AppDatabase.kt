package com.elevasign.player.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.elevasign.player.data.local.db.dao.AnnouncementDao
import com.elevasign.player.data.local.db.dao.LayoutZoneDao
import com.elevasign.player.data.local.db.dao.MediaItemDao
import com.elevasign.player.data.local.db.entity.AnnouncementEntity
import com.elevasign.player.data.local.db.entity.LayoutZoneEntity
import com.elevasign.player.data.local.db.entity.MediaItemEntity

@Database(
    entities = [
        MediaItemEntity::class,
        AnnouncementEntity::class,
        LayoutZoneEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mediaItemDao(): MediaItemDao
    abstract fun announcementDao(): AnnouncementDao
    abstract fun layoutZoneDao(): LayoutZoneDao
}
