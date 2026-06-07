package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.ChatDto
import com.dishcovery.app.data.datasource.model.ChatInputDto
import com.dishcovery.app.data.datasource.model.MessageCreateRequest
import com.dishcovery.app.data.datasource.model.MessageDto
import com.dishcovery.app.data.datasource.model.MessageUpdateRequest

interface ChatDataSource {
    suspend fun getChats(): List<ChatDto>
    suspend fun getChatById(id: Long): ChatDto
    suspend fun createChat(chatInput: ChatInputDto): ChatDto
    suspend fun updateChat(chatInput: ChatInputDto): ChatDto
    suspend fun deleteChat(id: Long)

    suspend fun getChatsByUserId(userId: Long): List<ChatDto>
    suspend fun getMessages(chatId: Long): List<MessageDto>
    suspend fun sendMessage(request: MessageCreateRequest): MessageDto
}
