package com.dishcovery.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.domain.model.User
import com.dishcovery.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for authentication
 */
class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Attempts to log in with the given credentials.
     * Updates [user] on success or [error] on failure.
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            repository.login(username, password)
                .onSuccess { user ->
                    _user.value = user
                    _error.value = null
                }
                .onFailure { throwable ->
                    _error.value = throwable.message
                }
        }
    }

    /** Clears the current user session. */
    fun logout() {
        _user.value = null
        _error.value = null
    }
}