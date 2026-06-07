package com.dishcovery.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.domain.model.Match
import com.dishcovery.app.domain.model.Chat
import com.dishcovery.app.domain.model.ExchangePlan
import com.dishcovery.app.domain.repository.MatchRepository
import com.dishcovery.app.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val repository: MatchRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _matches = MutableStateFlow<List<Match>>(emptyList())
    val matches: StateFlow<List<Match>> = _matches.asStateFlow()

    private val _generatedPlanId = MutableStateFlow<Long?>(null)
    val generatedPlanId: StateFlow<Long?> = _generatedPlanId.asStateFlow()

    private val _generatedChat = MutableStateFlow<Chat?>(null)
    val generatedChat: StateFlow<Chat?> = _generatedChat.asStateFlow()

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
        categoryId: Long,
        creatorName: String
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
                val plan = result.getOrNull()
                if (plan != null) {
                    // Generamos un nombre más significativo para el chat: [Creador] & [Partner] chat
                    val chatName = "$creatorName & ${plan.partnerName} chat"
                    
                    // Creamos el chat automáticamente al generar el plan
                    val chatResult = chatRepository.createChat(
                        senderId = plan.creatorId,
                        receiverId = plan.partnerId,
                        name = chatName,
                        avatarUrl = plan.partnerImageUrl,
                        planId = plan.id
                    )
                    if (chatResult.isSuccess) {
                        _generatedChat.value = chatResult.getOrNull()
                    }
                }
                _generatedPlanId.value = plan?.id
            } else {
                _errorMessage.value =
                    result.exceptionOrNull()?.message ?: "Error al generar el plan."
            }

            _isLoading.value = false
        }
    }

    fun clearGeneratedPlan() {
        _generatedPlanId.value = null
        _generatedChat.value = null
    }

    fun clearMatches() {
        _matches.value = emptyList()
    }
}
