package com.dishcovery.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.domain.model.StatPoint
import com.dishcovery.app.domain.repository.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminStatisticsViewModel @Inject constructor(
    private val repository: StatsRepository
) : ViewModel() {

    private val _activeUsers = MutableStateFlow<List<StatPoint>>(emptyList())
    val activeUsers: StateFlow<List<StatPoint>> = _activeUsers.asStateFlow()

    private val _matches = MutableStateFlow<List<StatPoint>>(emptyList())
    val matches: StateFlow<List<StatPoint>> = _matches.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init { loadStats() }

    fun loadStats(days: Int = 30) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getActiveUsers(days)
                .onSuccess { _activeUsers.value = it }
                .onFailure { _error.value = it.message }
            repository.getMatches(days)
                .onSuccess { _matches.value = it }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }
}