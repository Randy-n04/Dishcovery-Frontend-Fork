package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.ChatDto
import com.dishcovery.app.data.datasource.model.MessageCreateRequest
import com.dishcovery.app.data.datasource.model.MessageDto
import com.dishcovery.app.data.datasource.model.MessageUpdateRequest
import com.dishcovery.app.data.datasource.remote.ChatApiService
import javax.inject.Inject

/**
 * Implementación real de ChatDataSource conectada al backend.
 * Se han eliminado las dependencias de datos estáticos (AuthProvider).
 */
class ChatDataSourceImpl @Inject constructor(
    private val apiService: ChatApiService
) : ChatDataSource {

    override suspend fun getChats(userId: Long): List<ChatDto> {
        // Obtenemos los chats directamente de la API. 
        // El filtrado por userId lo hace el backend o se valida aquí.
        return apiService.getChats()
            .filter { it.userId1 == userId || it.userId2 == userId }
    }

    override suspend fun createChat(chat: ChatDto): ChatDto {
        return apiService.createChat(chat)
    }

    override suspend fun getMessages(chatId: Long): List<MessageDto> {
        return apiService.getMessagesByChat(chatId).filter { it.chatId == chatId }
    }

    override suspend fun sendMessage(request: MessageCreateRequest): MessageDto {
        return apiService.createMessage(request)
    }

    override suspend fun updateMessage(id: Long, request: MessageUpdateRequest): MessageDto {
        return apiService.updateMessage(id, request)
    }
}
