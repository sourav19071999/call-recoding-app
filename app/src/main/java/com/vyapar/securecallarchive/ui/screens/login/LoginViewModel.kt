package com.vyapar.securecallarchive.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vyapar.securecallarchive.data.local.preferences.SecurePreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val preferencesManager: SecurePreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    fun setEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun setPassword(newPassword: String) {
        _password.value = newPassword
    }

    fun login() {
        val email = _email.value.trim()
        val password = _password.value

        // Validation
        if (!validateEmail(email)) {
            _uiState.value = LoginUiState.Error("Please use a valid company email (user@vyapar.com or user@vyaparapp.in)")
            return
        }

        if (password.isEmpty()) {
            _uiState.value = LoginUiState.Error("Password cannot be empty")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = LoginUiState.Loading
                
                // Simulate network call
                delay(1500)
                
                // Mock authentication - in production, validate against backend
                if (authenticateUser(email, password)) {
                    preferencesManager.setUserEmail(email)
                    preferencesManager.setUserName(extractNameFromEmail(email))
                    preferencesManager.setLoggedIn(true)
                    _uiState.value = LoginUiState.Success
                    Timber.d("Login successful for: $email")
                } else {
                    _uiState.value = LoginUiState.Error("Invalid email or password")
                    Timber.d("Login failed: Invalid credentials")
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error("Login error: ${e.message}")
                Timber.e(e, "Login exception")
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        val vyaparPattern1 = Regex("^[a-zA-Z0-9._%+-]+@vyapar\\.com$")
        val vyaparPattern2 = Regex("^[a-zA-Z0-9._%+-]+@vyaparapp\\.in$")
        return vyaparPattern1.matches(email) || vyaparPattern2.matches(email)
    }

    private fun authenticateUser(email: String, password: String): Boolean {
        // In production, call your backend authentication API
        // For now, accept any valid email with non-empty password
        return validateEmail(email) && password.isNotEmpty()
    }

    private fun extractNameFromEmail(email: String): String {
        return email.substringBefore("@").replace(".", " ")
            .split(" ").joinToString(" ") { word ->
                word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            }
    }
}
