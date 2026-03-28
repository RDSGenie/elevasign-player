package com.elevasign.player.di

import android.content.Context
import androidx.room.Room
import com.elevasign.player.data.local.db.AppDatabase
import com.elevasign.player.data.local.db.dao.AnnouncementDao
import com.elevasign.player.data.local.db.dao.LayoutZoneDao
import com.elevasign.player.data.local.db.dao.MediaItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "elevasign_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideMediaItemDao(db: AppDatabase): MediaItemDao = db.mediaItemDao()

    @Provides
    fun provideAnnouncementDao(db: AppDatabase): AnnouncementDao = db.announcementDao()

    @Provides
    fun provideLayoutZoneDao(db: AppDatabase): LayoutZoneDao = db.layoutZoneDao()
}
