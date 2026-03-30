package com.elevasign.player.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elevasign.player.data.local.db.entity.MediaItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<MediaItemEntity>)

    @Query("SELECT * FROM media_items ORDER BY sort_order ASC")
    fun observeAll(): Flow<List<MediaItemEntity>>

    @Query("SELECT * FROM media_items WHERE zone_name = :zoneName ORDER BY sort_order ASC")
    fun observeByZone(zoneName: String): Flow<List<MediaItemEntity>>

    @Query("SELECT * FROM media_items ORDER BY sort_order ASC")
    suspend fun getAll(): List<MediaItemEntity>

    @Query("UPDATE media_items SET local_path = :path WHERE media_id = :mediaId")
    suspend fun updateLocalPath(mediaId: String, path: String)

    @Query("DELETE FROM media_items WHERE media_id NOT IN (:keepIds)")
    suspend fun deleteNotIn(keepIds: List<String>)

    @Query("DELETE FROM media_items")
    suspend fun deleteAll()
}
