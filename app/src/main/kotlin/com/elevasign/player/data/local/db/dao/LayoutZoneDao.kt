package com.elevasign.player.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elevasign.player.data.local.db.entity.LayoutZoneEntity

@Dao
interface LayoutZoneDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(zones: List<LayoutZoneEntity>)

    @Query("SELECT * FROM layout_zones ORDER BY z_index ASC")
    suspend fun getAll(): List<LayoutZoneEntity>

    @Query("DELETE FROM layout_zones")
    suspend fun deleteAll()
}
