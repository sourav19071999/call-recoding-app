package com.vyapar.securecallarchive.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vyapar.securecallarchive.data.local.preferences.SecurePreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferencesManager: SecurePreferencesManager
) : ViewModel() {

    val isLoggedIn = preferencesManager.isLoggedIn
    
    private val _hasCompletedSetup = MutableStateFlow(true)
    val hasCompletedSetup: StateFlow<Boolean> = _hasCompletedSetup

    init {
        viewModelScope.launch {
            preferencesManager.storagePath.collect { storagePath ->
                _hasCompletedSetup.value = storagePath.isNotEmpty()
            }
        }
    }
}
