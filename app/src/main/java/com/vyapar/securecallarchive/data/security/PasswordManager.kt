package com.vyapar.securecallarchive.data.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import timber.log.Timber

class PasswordManager(context: Context) {
    
    private val masterKey: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedSharedPreferences: SharedPreferences = 
        EncryptedSharedPreferences.create(
            context,
            "password_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    companion object {
        private const val PASSWORD_KEY = "admin_password"
        private const val DEFAULT_PASSWORD = "@Vyapar6202@"
    }

    fun initializeDefaultPassword() {
        val hasPassword = encryptedSharedPreferences.contains(PASSWORD_KEY)
        if (!hasPassword) {
            setPassword(DEFAULT_PASSWORD)
            Timber.d("Default password initialized")
        }
    }

    fun setPassword(password: String) {
        encryptedSharedPreferences.edit().putString(PASSWORD_KEY, password).apply()
        Timber.d("Password updated")
    }

    fun verifyPassword(inputPassword: String): Boolean {
        val storedPassword = encryptedSharedPreferences.getString(PASSWORD_KEY, DEFAULT_PASSWORD) ?: DEFAULT_PASSWORD
        return storedPassword == inputPassword
    }

    fun getPasswordHash(): String {
        return encryptedSharedPreferences.getString(PASSWORD_KEY, DEFAULT_PASSWORD) ?: DEFAULT_PASSWORD
    }

    fun changePassword(oldPassword: String, newPassword: String): Boolean {
        return if (verifyPassword(oldPassword)) {
            setPassword(newPassword)
            true
        } else {
            false
        }
    }
}
