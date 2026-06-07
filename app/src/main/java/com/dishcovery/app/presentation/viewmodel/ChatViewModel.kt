package com.dishcovery.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.domain.model.Chat
import com.dishcovery.app.domain.repository.ChatRepository
import com.dishcovery.app.domain.model.Profile
import com.dishcovery.app.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.dishcovery.app.domain.model.Message
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val profileRepository: ProfileRepository,
    private val globalErrorViewModel: GlobalErrorViewModel
) : ViewModel() {

    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats.asStateFlow()

    private val _publicNames = MutableStateFlow<Map<Long, String>>(emptyMap())
    val publicNames: StateFlow<Map<Long, String>> = _publicNames.asStateFlow()

    private val _currentChat = MutableStateFlow<Chat?>(null)
    val currentChat: StateFlow<Chat?> = _currentChat.asStateFlow()

    private val _otherParticipantProfile = MutableStateFlow<Profile?>(null)
    val otherParticipantProfile: StateFlow<Profile?> = _otherParticipantProfile.asStateFlow()

    private val _chatsError = MutableStateFlow<String?>(null)
    val chatsError: StateFlow<String?> = _chatsError.asStateFlow()

    private val _isLoadingChats = MutableStateFlow(false)
    val isLoadingChats: StateFlow<Boolean> = _isLoadingChats.asStateFlow()

    private val _isLoadingMessage = MutableStateFlow(false)
    val isLoadingMessage: StateFlow<Boolean> = _isLoadingMessage.asStateFlow()

    private val _isLoadingMessages = MutableStateFlow(false)
    val isLoadingMessages: StateFlow<Boolean> = _isLoadingMessages.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _messagesError = MutableStateFlow<String?>(null)
    val messagesError: StateFlow<String?> = _messagesError.asStateFlow()

    private val _sendError = MutableStateFlow<String?>(null)
    val sendError: StateFlow<String?> = _sendError.asStateFlow()

    fun loadChats(userId: Long) {
        viewModelScope.launch {
            _isLoadingChats.value = true
            repository.getChats(userId)
                .onSuccess { list ->
                    _chats.value = list
                    _chatsError.value = null
                    // Cargar nombres públicos de los participantes
                    list.forEach { chat ->
                        val otherId = if (chat.senderId == userId) chat.receiverId else chat.senderId
                        fetchPublicName(otherId)
                    }
                }
                .onFailure { throwable ->
                    _chats.value = emptyList()
                    _chatsError.value = "No se puede acceder a la lista de chats. ${throwable.message}"
                    globalErrorViewModel.showError(throwable)
                }
            _isLoadingChats.value = false
        }
    }

    private fun fetchPublicName(userId: Long) {
        if (_publicNames.value.containsKey(userId)) return
        viewModelScope.launch {
            profileRepository.getProfileByUserId(userId).onSuccess { profile ->
                _publicNames.value = _publicNames.value + (userId to profile.publicName)
            }
        }
    }

    fun loadChatAndMessages(chatId: Long, currentUserId: Long) {
        viewModelScope.launch {
            _isLoadingMessages.value = true
            _otherParticipantProfile.value = null
            _currentChat.value = null

            // Eliminar verificación de mensajes nuevos localmente al abrir el chat
            _chats.value = _chats.value.map {
                if (it.id == chatId) it.copy(unreadCount = 0) else it
            }
            
            // Cargar mensajes
            loadMessages(chatId)
            
            // Intentar encontrar el chat en cache primero
            var chat = _chats.value.find { it.id == chatId }
            
            if (chat == null) {
                repository.getChatById(chatId).onSuccess {
                    chat = it
                }.onFailure { globalErrorViewModel.showError(it) }
            }

            chat?.let { c ->
                _currentChat.value = c
                val otherUserId = if (c.senderId == currentUserId) c.receiverId else c.senderId
                fetchOtherParticipantProfile(otherUserId)
            }
            
            _isLoadingMessages.value = false
        }
    }

    private fun fetchOtherParticipantProfile(userId: Long) {
        viewModelScope.launch {
            profileRepository.getProfileByUserId(userId)
                .onSuccess { profile ->
                    _otherParticipantProfile.value = profile
                    _publicNames.value = _publicNames.value + (userId to profile.publicName)
                }
                .onFailure { globalErrorViewModel.showError(it) }
        }
    }

    fun loadMessages(chatId: Long) {
        viewModelScope.launch {
            _isLoadingMessages.value = true
            repository.getMessages(chatId)
                .onSuccess { allMessages ->
                    _messages.value = allMessages.filter { it.chatId == chatId }
                    _messagesError.value = null
                }
                .onFailure { throwable ->
                    _messages.value = emptyList()
                    _messagesError.value = "No se puede acceder a ese chat. ${throwable.message}"
                    globalErrorViewModel.showError(throwable)
                }
            _isLoadingMessages.value = false
        }
    }

    fun sendMessage(chatId: Long, senderId: Long, text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            _isLoadingMessage.value = true
            _sendError.value = null
            repository.sendMessage(chatId, senderId, text)
                .onSuccess { createdMessage ->
                    _messages.value = _messages.value + createdMessage
                }
                .onFailure { throwable ->
                    _sendError.value = throwable.message ?: "No se pudo enviar el mensaje"
                    globalErrorViewModel.showError(throwable)
                }
            _isLoadingMessage.value = false
        }
    }

    fun clearSendError() {
        _sendError.value = null
    }

    fun editMessage(id: Long, text: String, chatId: Long) {
        viewModelScope.launch {
            _isLoadingMessage.value = true
            repository.updateMessage(id, text)
                .onSuccess { updatedMessage ->
                    _messages.value = _messages.value.map { message ->
                        if (message.id == id)
                            updatedMessage.copy(edited = true)
                        else message
                    }
                    _sendError.value = null
                }
                .onFailure { throwable ->
                    _sendError.value = "No se pudo editar el mensaje."
                    globalErrorViewModel.showError(throwable)
                }
            _isLoadingMessage.value = false
        }
    }
}
