package com.vyapar.securecallarchive.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vyapar.securecallarchive.data.local.preferences.SecurePreferencesManager
import com.vyapar.securecallarchive.data.security.PasswordManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: SecurePreferencesManager,
    private val passwordManager: PasswordManager
) : ViewModel() {

    val userName: StateFlow<String> = preferencesManager.userName
    val userEmail: StateFlow<String> = preferencesManager.userEmail
    val biometricEnabled: StateFlow<Boolean> = preferencesManager.biometricEnabled
    val recordingQuality: StateFlow<String> = preferencesManager.recordingQuality

    fun setBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setBiometricEnabled(enabled)
            Timber.d("Biometric enabled: $enabled")
        }
    }

    fun setRecordingQuality(quality: String) {
        viewModelScope.launch {
            preferencesManager.setRecordingQuality(quality)
            Timber.d("Recording quality set to: $quality")
        }
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            if (passwordManager.changePassword(oldPassword, newPassword)) {
                Timber.d("Password changed successfully")
            } else {
                Timber.d("Failed to change password: old password incorrect")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            preferencesManager.logout()
            Timber.d("User logged out")
        }
    }
}
