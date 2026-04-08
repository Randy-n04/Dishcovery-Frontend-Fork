package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.ChatDto
import com.dishcovery.app.data.datasource.model.MessageCreateRequest
import com.dishcovery.app.data.datasource.model.MessageDto
import com.dishcovery.app.data.datasource.model.MessageUpdateRequest

interface ChatDataSource {
    suspend fun getChats(): List<ChatDto>
    suspend fun getMessages(chatId: Long): List<MessageDto>
    suspend fun sendMessage(request: MessageCreateRequest): MessageDto
    suspend fun updateMessage(id: Long, request: MessageUpdateRequest): MessageDto
}