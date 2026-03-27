package com.dishcovery.app.presentation.viewmodel

import com.dishcovery.app.domain.model.Profile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the profile screen.
 * Loads and exposes the profile of the given user.
 */
class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /** Loads the profile for [userId]. */
    fun loadProfile(userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getProfileByUserId(userId)
                .onSuccess { profile ->
                    _profile.value = profile
                    _error.value = null
                }
                .onFailure { e ->
                    _error.value = e.message
                }
            _isLoading.value = false
        }
    }
}