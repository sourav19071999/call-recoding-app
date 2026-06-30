package com.vyapar.securecallarchive.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "secure_prefs")

class SecurePreferencesManager(private val context: Context) {
    
    companion object {
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val STORAGE_PATH = stringPreferencesKey("storage_path")
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
        private val AUTO_CLEANUP_ENABLED = booleanPreferencesKey("auto_cleanup_enabled")
        private val AUTO_CLEANUP_DAYS = stringPreferencesKey("auto_cleanup_days")
        private val RECORDING_QUALITY = stringPreferencesKey("recording_quality")
    }

    val userEmail: Flow<String> = context.dataStore.data.map { it[USER_EMAIL] ?: "" }
    val userName: Flow<String> = context.dataStore.data.map { it[USER_NAME] ?: "" }
    val storagePath: Flow<String> = context.dataStore.data.map { it[STORAGE_PATH] ?: "" }
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { it[IS_LOGGED_IN] ?: false }
    val biometricEnabled: Flow<Boolean> = context.dataStore.data.map { it[BIOMETRIC_ENABLED] ?: false }
    val autoCleanupEnabled: Flow<Boolean> = context.dataStore.data.map { it[AUTO_CLEANUP_ENABLED] ?: false }
    val autoCleanupDays: Flow<Int> = context.dataStore.data.map { (it[AUTO_CLEANUP_DAYS] ?: "30").toIntOrNull() ?: 30 }
    val recordingQuality: Flow<String> = context.dataStore.data.map { it[RECORDING_QUALITY] ?: "HIGH" }

    suspend fun setUserEmail(email: String) {
        context.dataStore.edit { it[USER_EMAIL] = email }
    }

    suspend fun setUserName(name: String) {
        context.dataStore.edit { it[USER_NAME] = name }
    }

    suspend fun setStoragePath(path: String) {
        context.dataStore.edit { it[STORAGE_PATH] = path }
    }

    suspend fun setLoggedIn(loggedIn: Boolean) {
        context.dataStore.edit { it[IS_LOGGED_IN] = loggedIn }
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        context.dataStore.edit { it[BIOMETRIC_ENABLED] = enabled }
    }

    suspend fun setAutoCleanupEnabled(enabled: Boolean) {
        context.dataStore.edit { it[AUTO_CLEANUP_ENABLED] = enabled }
    }

    suspend fun setAutoCleanupDays(days: Int) {
        context.dataStore.edit { it[AUTO_CLEANUP_DAYS] = days.toString() }
    }

    suspend fun setRecordingQuality(quality: String) {
        context.dataStore.edit { it[RECORDING_QUALITY] = quality }
    }

    suspend fun logout() {
        context.dataStore.edit { 
            it[IS_LOGGED_IN] = false
            it[USER_EMAIL] = ""
            it[USER_NAME] = ""
        }
    }

    suspend fun clearAllData() {
        context.dataStore.edit { it.clear() }
    }
}
