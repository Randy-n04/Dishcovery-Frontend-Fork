package com.dishcovery.app.domain.repository

import com.dishcovery.app.domain.model.Chat
import com.dishcovery.app.domain.model.Message

interface ChatRepository {
    suspend fun getChats(username: String): Result<List<Chat>>
    suspend fun getMessages(chatId: Long): Result<List<Message>>
    suspend fun sendMessage(chatId: Long, senderId: Long, text: String): Result<Message>
    suspend fun updateMessage(id: Long, text: String): Result<Message>
}