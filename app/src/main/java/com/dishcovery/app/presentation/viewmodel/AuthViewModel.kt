package com.dishcovery.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.domain.model.User
import com.dishcovery.app.domain.repository.AuthRepository
import com.dishcovery.app.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la gestión de autenticación real conectada al backend
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /**
     * Realiza el login contra el backend. 
     * El repositorio se encarga de guardar el JWT en TokenManager.
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            repository.login(username, password)
                .onSuccess { loggedUser ->
                    _user.value = loggedUser
                    _error.value = null
                }
                .onFailure { throwable ->
                    _error.value = throwable.message ?: "Error al iniciar sesión"
                }
            
            _isLoading.value = false
        }
    }

    /**
     * Realiza el logout llamando al backend y limpiando la sesión local.
     */
    fun logout() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.logout()
                .onSuccess {
                    _user.value = null
                    _error.value = null
                }
                .onFailure { throwable ->
                    // Aun si falla la red, ya se limpio el token localmente en el repo
                    _user.value = null
                    _error.value = throwable.message
                }
            _isLoading.value = false
        }
    }

    /**
     * Verifica si hay una sesión activa al arrancar la app
     */
    fun checkSession() {
        val token = tokenManager.getToken()
        if (token != null) {
            // Aquí podrías llamar a un endpoint /me o /profile para recuperar el usuario
            // Por ahora lo dejamos para implementar cuando tengas ese endpoint
        }
    }
}
