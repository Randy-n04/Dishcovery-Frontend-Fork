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

    private val usernamesById: Map<Long, String> by lazy {
        AuthProvider.getUsers().associate { it.id to it.username }
    }

    override suspend fun getChats(userId: Long): List<ChatDto> {
        // Traemos todos y filtramos localmente por ambos participantes.
        return apiService.getChats()
            .filter { it.userId1 == userId || it.userId2 == userId }
            .map { chat ->
                val otherUserId = if (chat.userId1 == userId) chat.userId2 else chat.userId1
                val otherUsername = usernamesById[otherUserId] ?: chat.name
                val otherAvatarUrl = ProfileProvider.getProfileByUserId(otherUserId)?.photoUrl
                    ?: chat.avatarUrl
                chat.copy(name = otherUsername, avatarUrl = otherAvatarUrl)
            }
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