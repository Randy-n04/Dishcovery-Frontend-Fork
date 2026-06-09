package com.dishcovery.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.domain.error.DomainError
import com.dishcovery.app.domain.model.ExchangePlan
import com.dishcovery.app.domain.model.PlanStatus
import com.dishcovery.app.domain.model.Profile
import com.dishcovery.app.domain.repository.ExchangePlanRepository
import com.dishcovery.app.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.delay

@HiltViewModel
class ExchangePlanViewModel @Inject constructor(
    private val repository: ExchangePlanRepository,
    private val profileRepository: ProfileRepository,
    private val globalErrorViewModel: GlobalErrorViewModel
) : ViewModel() {

    private val _plan = MutableStateFlow<ExchangePlan?>(null)
    val plan: StateFlow<ExchangePlan?> = _plan.asStateFlow()

    private val _allPlans = MutableStateFlow<List<ExchangePlan>>(emptyList())
    val allPlans: StateFlow<List<ExchangePlan>> = _allPlans.asStateFlow()

    private val _partnerProfile = MutableStateFlow<Profile?>(null)
    val partnerProfile: StateFlow<Profile?> = _partnerProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadPlan(planId: Long? = null, currentUserId: Long? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _partnerProfile.value = null

            val result = if (planId != null) {
                repository.getPlanById(planId)
            } else {
                repository.getAllPlans().map { plans ->
                    plans.firstOrNull() ?: throw NoSuchElementException("No hay planes.")
                }
            }

            result.onSuccess { plan ->
                _plan.value = plan
                if (plan != null && currentUserId != null) {
                    val otherUserId = if (plan.creatorId == currentUserId) plan.partnerId else plan.creatorId
                    loadPartnerProfile(otherUserId)
                }
            }.onFailure { handleFailure(it) }

            _isLoading.value = false
        }
    }

    fun loadAllPlans() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            repository.getAllPlans()
                .onSuccess { _allPlans.value = it }
                .onFailure { handleFailure(it) }
            _isLoading.value = false
        }
    }

    fun loadPlanByChatId(chatId: Long, currentUserId: Long? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            repository.getPlanByChatId(chatId)
                .onSuccess { plan ->
                    _plan.value = plan
                    if (plan != null && currentUserId != null) {
                        val otherUserId = if (plan.creatorId == currentUserId) plan.partnerId else plan.creatorId
                        loadPartnerProfile(otherUserId)
                    }
                }
                .onFailure { handleFailure(it) }
            _isLoading.value = false
        }
    }

    private fun loadPartnerProfile(userId: Long) {
        viewModelScope.launch {
            profileRepository.getProfileByUserId(userId)
                .onFailure { globalErrorViewModel.showError(it) }
                .onSuccess { _partnerProfile.value = it }
        }
    }

    private fun handleFailure(throwable: Throwable) {
        val domainError = DomainError.fromThrowable(throwable)
        _errorMessage.value = domainError.message
        globalErrorViewModel.showError(throwable)
    }

    fun toggleSessionCompletion(index: Int) {
        val currentPlan = _plan.value ?: return
        val newSessions = currentPlan.sessions.toMutableList()
        if (index in newSessions.indices) {
            newSessions[index] = newSessions[index].copy(isCompleted = !newSessions[index].isCompleted)
            val completedCount = newSessions.count { it.isCompleted }
            _plan.value = currentPlan.copy(sessions = newSessions, progress = completedCount)
        }
    }

    fun savePlan() {
        val currentPlan = _plan.value ?: return

        viewModelScope.launch {
            _isSaving.value = true
            _saveSuccess.value = false

            repository.updatePlan(currentPlan)
                .onSuccess {
                    _plan.value = it
                    _isSaving.value = false
                    _saveSuccess.value = true

                    delay(1500)

                    _saveSuccess.value = false
                }
                .onFailure {
                    _isSaving.value = false
                    _saveSuccess.value = false
                    handleFailure(it)
                }
        }
    }

    fun acceptPlan(onSuccess: () -> Unit = {}) {
        val currentPlan = _plan.value ?: return
        viewModelScope.launch {
            _isSaving.value = true
            repository.updatePlan(currentPlan.copy(status = PlanStatus.ACCEPTED))
                .onSuccess { 
                    _plan.value = it
                    onSuccess() 
                }
                .onFailure { handleFailure(it) }
            _isSaving.value = false
        }
    }

    fun rejectPlan(onSuccess: () -> Unit) {
        val currentPlan = _plan.value ?: return
        viewModelScope.launch {
            _isSaving.value = true
            repository.updatePlan(currentPlan.copy(status = PlanStatus.REJECTED))
                .onSuccess { onSuccess() }
                .onFailure { handleFailure(it) }
            _isSaving.value = false
        }
    }
}
