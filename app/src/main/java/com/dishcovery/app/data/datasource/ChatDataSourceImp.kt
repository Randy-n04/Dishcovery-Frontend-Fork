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

    override suspend fun getChats(userId: Long): List<ChatDto> {
        // Obtenemos todos los chats y filtramos localmente ya que el mock no lo hace
        return apiService.getChats(userId).filter { it.userId == userId }
    }

    override suspend fun createChat(chat: ChatDto): ChatDto {
        return apiService.createChat(chat)
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