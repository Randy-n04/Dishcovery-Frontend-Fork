package com.dishcovery.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.domain.model.ExchangePlan
import com.dishcovery.app.domain.model.ExchangeSession
import com.dishcovery.app.domain.model.PlanStatus
import com.dishcovery.app.domain.repository.ExchangePlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangePlanViewModel @Inject constructor(
    private val repository: ExchangePlanRepository
) : ViewModel() {

    private val _plan = MutableStateFlow<ExchangePlan?>(null)
    val plan: StateFlow<ExchangePlan?> = _plan.asStateFlow()

    private val _allPlans = MutableStateFlow<List<ExchangePlan>>(emptyList())
    val allPlans: StateFlow<List<ExchangePlan>> = _allPlans.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadAllPlans() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = repository.getAllPlans()
            if (result.isSuccess) {
                _allPlans.value = result.getOrNull() ?: emptyList()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Error al cargar los planes."
            }
            _isLoading.value = false
        }
    }

    fun loadPlan(planId: Long? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = if (planId != null) {
                repository.getPlanById(planId)
            } else {
                repository.getAllPlans().map { plans ->
                    plans.firstOrNull()
                        ?: throw NoSuchElementException("No hay planes de intercambio disponibles.")
                }
            }

            if (result.isSuccess) {
                _plan.value = result.getOrNull()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Error al cargar el plan."
            }

            _isLoading.value = false
        }
    }

    fun loadPlanByChatId(chatId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = repository.getPlanByChatId(chatId)
            if (result.isSuccess) {
                _plan.value = result.getOrNull()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Error al cargar el plan por chat."
            }
            _isLoading.value = false
        }
    }

    fun updateSession(index: Int, updatedSession: ExchangeSession) {
        val currentPlan = _plan.value ?: return
        val newSessions = currentPlan.sessions.toMutableList()

        if (index in newSessions.indices) {
            newSessions[index] = updatedSession
            
            val completedCount = newSessions.count { it.isCompleted }
            
            _plan.value = currentPlan.copy(
                sessions = newSessions,
                progress = completedCount,
                totalSessions = newSessions.size
            )
        }
    }

    fun toggleSessionCompletion(index: Int) {
        val currentPlan = _plan.value ?: return
        val newSessions = currentPlan.sessions.toMutableList()

        if (index in newSessions.indices) {
            val session = newSessions[index]
            newSessions[index] = session.copy(isCompleted = !session.isCompleted)
            
            // Recalcular progreso como cantidad de sesiones completadas
            val completedCount = newSessions.count { it.isCompleted }
            val total = newSessions.size
            
            _plan.value = currentPlan.copy(
                sessions = newSessions,
                progress = completedCount,
                totalSessions = total
            )
        }
    }

    fun updateUserNote(note: String) {
        val currentPlan = _plan.value ?: return
        _plan.value = currentPlan.copy(userNote = note)
    }

    fun savePlan(onSuccess: (() -> Unit)? = null) {
        val currentPlan = _plan.value ?: return

        viewModelScope.launch {
            _isSaving.value = true
            _errorMessage.value = null

            val result = repository.updatePlan(currentPlan)

            if (result.isSuccess) {
                _plan.value = result.getOrNull()
                onSuccess?.invoke()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Error al guardar el plan."
            }

            _isSaving.value = false
        }
    }

    fun acceptPlan(onSuccess: () -> Unit) {
        val currentPlan = _plan.value ?: return

        viewModelScope.launch {
            _isSaving.value = true
            _errorMessage.value = null

            val acceptedPlan = currentPlan.copy(status = PlanStatus.ACCEPTED)
            val result = repository.updatePlan(acceptedPlan)

            if (result.isSuccess) {
                _plan.value = result.getOrNull()
                onSuccess()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Error al aceptar el plan."
            }

            _isSaving.value = false
        }
    }

    fun rejectPlan(onSuccess: () -> Unit) {
        val currentPlan = _plan.value ?: return

        viewModelScope.launch {
            _isSaving.value = true
            _errorMessage.value = null

            val rejectedPlan = currentPlan.copy(status = PlanStatus.REJECTED)
            val result = repository.updatePlan(rejectedPlan)

            if (result.isSuccess) {
                _plan.value = result.getOrNull()
                onSuccess()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Error al rechazar el plan."
            }

            _isSaving.value = false
        }
    }
}
