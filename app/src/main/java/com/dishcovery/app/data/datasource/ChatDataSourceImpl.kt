package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.ChatDto
import com.dishcovery.app.data.datasource.model.ChatInputDto
import com.dishcovery.app.data.datasource.model.MessageCreateRequest
import com.dishcovery.app.data.datasource.model.MessageDto
import com.dishcovery.app.data.datasource.model.MessageUpdateRequest
import com.dishcovery.app.data.datasource.remote.ChatApiService
import javax.inject.Inject

class ChatDataSourceImpl @Inject constructor(
    private val apiService: ChatApiService
) : ChatDataSource {

    override suspend fun getChats(): List<ChatDto> {
        return apiService.findAll()
    }

    override suspend fun getChatById(id: Long): ChatDto {
        return apiService.findById(id)
    }

    override suspend fun createChat(chatInput: ChatInputDto): ChatDto {
        return apiService.create(chatInput)
    }

    override suspend fun updateChat(chatInput: ChatInputDto): ChatDto {
        return apiService.update(chatInput)
    }

    override suspend fun deleteChat(id: Long) {
        apiService.deleteById(id)
    }

    override suspend fun getChatsByUserId(userId: Long): List<ChatDto> {
        return apiService.findByUserId(userId)
    }

    override suspend fun getMessages(chatId: Long): List<MessageDto> {
        return apiService.getMessagesByChat(chatId)
    }

    override suspend fun sendMessage(request: MessageCreateRequest): MessageDto {
        return apiService.createMessage(request)
    }
}
