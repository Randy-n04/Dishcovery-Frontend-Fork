package com.dishcovery.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.domain.model.Match
import com.dishcovery.app.domain.repository.MatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val repository: MatchRepository
) : ViewModel() {

    private val _matches = MutableStateFlow<List<Match>>(emptyList())
    val matches: StateFlow<List<Match>> = _matches.asStateFlow()

    private val _generatedPlanId = MutableStateFlow<Long?>(null)
    val generatedPlanId: StateFlow<Long?> = _generatedPlanId.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun findMatches(profileId: Long, categoryIds: List<Long>) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _matches.value = emptyList()

            repository.findMatches(profileId, categoryIds)
                .onSuccess {
                    _matches.value = it
                }
                .onFailure {
                    _errorMessage.value = it.message ?: "Error al buscar candidatos."
                }
            _isLoading.value = false
        }
    }

    fun generatePlan(
        profileId1: Long,
        profileId2: Long,
        categoryId: Long
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.generatePlan(
                profileId1 = profileId1,
                profileId2 = profileId2,
                categoryId = categoryId
            )

            if (result.isSuccess) {
                _generatedPlanId.value = result.getOrNull()?.id
            } else {
                _errorMessage.value =
                    result.exceptionOrNull()?.message ?: "Error al generar el plan."
            }

            _isLoading.value = false
        }
    }

    fun clearGeneratedPlan() {
        _generatedPlanId.value = null
    }

    fun clearMatches() {
        _matches.value = emptyList()
    }
}