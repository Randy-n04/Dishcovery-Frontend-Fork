package com.dishcovery.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.domain.model.Chat
import com.dishcovery.app.domain.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.dishcovery.app.domain.model.Message


class ChatViewModel(
    private val repository: ChatRepository
) : ViewModel() {

    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats

    fun loadChats(userId: Long) {
        viewModelScope.launch {
            repository.getChats(userId)
                .onSuccess { _chats.value = it }
        }
    }

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    fun loadMessages(chatId: Long) {
        viewModelScope.launch {
            repository.getMessages(chatId)
                .onSuccess { _messages.value = it }
                .onFailure { _messages.value = emptyList() }
        }
    }
}