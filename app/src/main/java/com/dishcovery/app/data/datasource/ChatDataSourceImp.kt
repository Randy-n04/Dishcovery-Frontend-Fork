package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.ChatDto
import com.dishcovery.app.data.datasource.model.MessageCreateRequest
import com.dishcovery.app.data.datasource.model.MessageDto
import com.dishcovery.app.data.datasource.model.MessageUpdateRequest
import com.dishcovery.app.data.datasource.remote.ChatApiService
import javax.inject.Inject

class ChatDataSourceImpl @Inject constructor(
    private val apiService: ChatApiService
) : ChatDataSource {

    override suspend fun getChats(): List<ChatDto> {
        return apiService.getChats()
    }

    override suspend fun getMessages(chatId: Long): List<MessageDto> {
        return apiService.getMessagesByChat(chatId)
    }

    override suspend fun sendMessage(request: MessageCreateRequest): MessageDto {
        return apiService.createMessage(request)
    }

    override suspend fun updateMessage(id: Long, request: MessageUpdateRequest): MessageDto {
        return apiService.updateMessage(id, request)
    }
}