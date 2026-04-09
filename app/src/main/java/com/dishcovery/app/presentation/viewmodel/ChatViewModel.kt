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

    fun loadChats(userId: Long) {
        viewModelScope.launch {
            repository.getChats(userId)
                .onSuccess {
                    _chats.value = it
                    _chatsError.value = null
                }
                .onFailure { throwable ->
                    _chats.value = emptyList()
                    _chatsError.value = throwable.message ?: "No se pudieron cargar los chats"
                }
        }
    }

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _messagesError = MutableStateFlow<String?>(null)
    val messagesError: StateFlow<String?> = _messagesError

    private val _sendError = MutableStateFlow<String?>(null)
    val sendError: StateFlow<String?> = _sendError

    fun loadMessages(chatId: Long) {
        viewModelScope.launch {
            repository.getMessages(chatId)
                .onSuccess { allMessages ->
                    // El endpoint puede devolver todo; filtramos en app segun el chat abierto.
                    _messages.value = allMessages.filter { message ->
                        message.chatId == chatId
                    }
                    _messagesError.value = null
                }
                .onFailure { throwable ->
                    _messages.value = emptyList()
                    _messagesError.value = throwable.message ?: "No se pudieron cargar los mensajes"
                }
        }
    }

    fun sendMessage(chatId: Long, senderId: Long, text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            _sendError.value = null
            repository.sendMessage(chatId, senderId, text)
                .onSuccess { createdMessage ->
                    _messages.value = _messages.value + createdMessage
                }
                .onFailure { throwable ->
                    _sendError.value = throwable.message ?: "No se pudo enviar el mensaje"
                }
        }
    }

    fun clearSendError() {
        _sendError.value = null
    }

    fun editMessage(id: Long, text: String, chatId: Long) {
        viewModelScope.launch {
            repository.updateMessage(id, text)
                .onSuccess {
                    loadMessages(chatId)
                }
        }
    }
}