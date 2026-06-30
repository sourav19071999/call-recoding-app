package com.vyapar.securecallarchive.ui.screens.firstlaunch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vyapar.securecallarchive.data.local.preferences.SecurePreferencesManager
import com.vyapar.securecallarchive.data.security.PasswordManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirstLaunchViewModel @Inject constructor(
    private val preferencesManager: SecurePreferencesManager,
    private val passwordManager: PasswordManager
) : ViewModel() {

    private val _setupStep = MutableStateFlow(SetupStep.CHOOSE_STORAGE)
    val setupStep: StateFlow<SetupStep> = _setupStep

    fun moveToPasswordSetup() {
        _setupStep.value = SetupStep.SET_PASSWORD
    }

    fun setPassword(password: String) {
        viewModelScope.launch {
            passwordManager.setPassword(password)
        }
    }

    fun completeSetup() {
        viewModelScope.launch {
            _setupStep.value = SetupStep.COMPLETE
        }
    }
}
