package com.elevasign.player.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "player_prefs")

@Singleton
class PlayerPreferences @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    companion object {
        private val KEY_SCREEN_ID = stringPreferencesKey("screen_id")
        private val KEY_SCREEN_NAME = stringPreferencesKey("screen_name")
        private val KEY_LAYOUT_TEMPLATE = stringPreferencesKey("layout_template")
        private val KEY_CONTENT_VERSION = longPreferencesKey("content_version")
        private val KEY_MANIFEST_HASH = stringPreferencesKey("manifest_hash")
        private val KEY_CURRENT_PLAYLIST_ID = stringPreferencesKey("current_playlist_id")
        private val KEY_CURRENT_PLAYLIST_NAME = stringPreferencesKey("current_playlist_name")
    }

    val screenId: Flow<String?> = context.dataStore.data.map { it[KEY_SCREEN_ID] }
    val screenName: Flow<String?> = context.dataStore.data.map { it[KEY_SCREEN_NAME] }
    val layoutTemplate: Flow<String?> = context.dataStore.data.map { it[KEY_LAYOUT_TEMPLATE] }
    val contentVersion: Flow<Long> = context.dataStore.data.map { it[KEY_CONTENT_VERSION] ?: 0L }
    val manifestHash: Flow<String?> = context.dataStore.data.map { it[KEY_MANIFEST_HASH] }
    val currentPlaylistId: Flow<String?> = context.dataStore.data.map { it[KEY_CURRENT_PLAYLIST_ID] }
    val currentPlaylistName: Flow<String?> = context.dataStore.data.map { it[KEY_CURRENT_PLAYLIST_NAME] }

    suspend fun saveRegistration(screenId: String, name: String, layoutTemplate: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SCREEN_ID] = screenId
            prefs[KEY_SCREEN_NAME] = name
            prefs[KEY_LAYOUT_TEMPLATE] = layoutTemplate
        }
    }

    suspend fun updateSyncState(contentVersion: Long, manifestHash: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_CONTENT_VERSION] = contentVersion
            prefs[KEY_MANIFEST_HASH] = manifestHash
        }
    }

    suspend fun updateCurrentPlaylist(playlistId: String, playlistName: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_CURRENT_PLAYLIST_ID] = playlistId
            prefs[KEY_CURRENT_PLAYLIST_NAME] = playlistName
        }
    }

    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }

    suspend fun resetSyncState() {
        context.dataStore.edit { prefs ->
            prefs[KEY_CONTENT_VERSION] = 0L
            prefs.remove(KEY_MANIFEST_HASH)
        }
    }
}
