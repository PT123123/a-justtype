package com.justtype.shellkeyboard.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * User preferences using DataStore.
 */
class UserPrefs(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

        val KEY_THEME = stringPreferencesKey("theme")
        val KEY_KEYBOARD_HEIGHT = intPreferencesKey("keyboard_height")
        val KEY_HAPTIC_FEEDBACK = booleanPreferencesKey("haptic_feedback")
        val KEY_SOUND_FEEDBACK = booleanPreferencesKey("sound_feedback")
        val KEY_CURRENT_SCHEMA = stringPreferencesKey("current_schema")
        val KEY_CLOUD_SYNC = booleanPreferencesKey("cloud_sync")
    }

    val theme: Flow<String> = context.dataStore.data.map { it[KEY_THEME] ?: "system" }
    val keyboardHeight: Flow<Int> = context.dataStore.data.map { it[KEY_KEYBOARD_HEIGHT] ?: 80 }
    val hapticFeedback: Flow<Boolean> = context.dataStore.data.map { it[KEY_HAPTIC_FEEDBACK] ?: true }
    val soundFeedback: Flow<Boolean> = context.dataStore.data.map { it[KEY_SOUND_FEEDBACK] ?: true }
    val currentSchema: Flow<String> = context.dataStore.data.map { it[KEY_CURRENT_SCHEMA] ?: "luna_pinyin" }
    val cloudSync: Flow<Boolean> = context.dataStore.data.map { it[KEY_CLOUD_SYNC] ?: false }

    suspend fun setTheme(theme: String) {
        context.dataStore.edit { it[KEY_THEME] = theme }
    }

    suspend fun setKeyboardHeight(height: Int) {
        context.dataStore.edit { it[KEY_KEYBOARD_HEIGHT] = height }
    }

    suspend fun setHapticFeedback(enabled: Boolean) {
        context.dataStore.edit { it[KEY_HAPTIC_FEEDBACK] = enabled }
    }

    suspend fun setSoundFeedback(enabled: Boolean) {
        context.dataStore.edit { it[KEY_SOUND_FEEDBACK] = enabled }
    }

    suspend fun setCurrentSchema(schemaId: String) {
        context.dataStore.edit { it[KEY_CURRENT_SCHEMA] = schemaId }
    }

    suspend fun setCloudSync(enabled: Boolean) {
        context.dataStore.edit { it[KEY_CLOUD_SYNC] = enabled }
    }
}
