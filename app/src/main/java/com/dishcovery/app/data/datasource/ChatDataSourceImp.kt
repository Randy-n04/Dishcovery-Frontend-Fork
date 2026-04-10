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
        // Algunas APIs de prueba como MockAPI pueden hacer búsqueda parcial (ej: filtrar por '2' trae '12').
        // Aplicamos un filtro extra local para asegurar coincidencia exacta del userId.
        return apiService.getChats(userId).filter { it.userId == userId }
    }

    override suspend fun createChat(chat: ChatDto): ChatDto {
        return apiService.createChat(chat)
    }

    override suspend fun getMessages(chatId: Long): List<MessageDto> {
        // Aplicamos filtro exacto también para los mensajes por seguridad.
        return apiService.getMessagesByChat(chatId).filter { it.chatId == chatId }
    }

    override suspend fun sendMessage(request: MessageCreateRequest): MessageDto {
        return apiService.createMessage(request)
    }

    override suspend fun updateMessage(id: Long, request: MessageUpdateRequest): MessageDto {
        return apiService.updateMessage(id, request)
    }
}