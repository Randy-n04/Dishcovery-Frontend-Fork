package com.dishcovery.app.domain.repository

import com.dishcovery.app.domain.model.Chat
import com.dishcovery.app.domain.model.Message

interface ChatRepository {


    suspend fun getChats(userId: Long): Result<List<Chat>>


    suspend fun getMessages(chatId: Long): Result<List<Message>>

    suspend fun sendMessage(chatId: Long, senderId: Long, text: String)
}