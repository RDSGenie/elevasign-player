package com.elevasign.player.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elevasign.player.data.local.db.entity.AnnouncementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnouncementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<AnnouncementEntity>)

    /**
     * Returns active announcements where:
     * - is_active = true
     * - starts_at <= now
     * - expires_at is null OR expires_at > now
     * Uses ISO 8601 string comparison (lexicographic works for UTC timestamps)
     */
    @Query("""
        SELECT * FROM announcements
        WHERE is_active = 1
          AND starts_at <= :nowIso
          AND (expires_at IS NULL OR expires_at > :nowIso)
        ORDER BY priority DESC
    """)
    fun observeActive(nowIso: String): Flow<List<AnnouncementEntity>>

    @Query("DELETE FROM announcements")
    suspend fun deleteAll()
}
