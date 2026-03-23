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

    // Estado del usuario logueado
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Login function
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = repository.login(username, password)

            result.onSuccess { user ->
                _user.value = user
                _error.value = null
            }.onFailure {
                _error.value = it.message
            }
        }
    }
}