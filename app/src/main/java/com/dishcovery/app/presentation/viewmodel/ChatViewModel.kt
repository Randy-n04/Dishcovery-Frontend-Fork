package com.dishcovery.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.domain.model.Chat
import com.dishcovery.app.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.dishcovery.app.domain.model.Message
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats

    private val _chatsError = MutableStateFlow<String?>(null)
    val chatsError: StateFlow<String?> = _chatsError

    private val _isLoadingChats = MutableStateFlow(false)
    val isLoadingChats: StateFlow<Boolean> = _isLoadingChats

    private val _isLoadingMessage = MutableStateFlow(false)
    val isLoadingMessage: StateFlow<Boolean> = _isLoadingMessage

    private val _isLoadingMessages = MutableStateFlow(false)
    val isLoadingMessages: StateFlow<Boolean> = _isLoadingMessages

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _messagesError = MutableStateFlow<String?>(null)
    val messagesError: StateFlow<String?> = _messagesError

    private val _sendError = MutableStateFlow<String?>(null)
    val sendError: StateFlow<String?> = _sendError

    fun loadChats(userId: Long) {
        viewModelScope.launch {
            _isLoadingChats.value = true
            repository.getChats(userId)
                .onSuccess {
                    _chats.value = it
                    _chatsError.value = null
                }
                .onFailure { throwable ->
                    _chats.value = emptyList()
                    _chatsError.value = "No se puede acceder a la lista de chats. ${throwable.message}"
                }
            _isLoadingChats.value = false
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
                            updatedMessage.copy(senderId = message.senderId)
                        else message
                    }
                    _sendError.value = null
                }
                .onFailure { throwable ->
                    _sendError.value = "No se pudo editar el mensaje."
                }
            _isLoadingMessage.value = false
        }
    }


}