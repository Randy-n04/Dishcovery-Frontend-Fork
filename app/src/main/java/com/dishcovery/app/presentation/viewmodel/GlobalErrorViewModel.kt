package com.dishcovery.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.dishcovery.app.domain.error.DomainError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalErrorViewModel @Inject constructor() : ViewModel() {
    private val _error = MutableStateFlow<DomainError?>(null)
    val error: StateFlow<DomainError?> = _error.asStateFlow()

    fun showError(throwable: Throwable) {
        _error.value = DomainError.fromThrowable(throwable)
    }

    fun clearError() {
        _error.value = null
    }
}
