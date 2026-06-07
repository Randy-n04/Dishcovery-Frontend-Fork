package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.ChatDataSource
import com.dishcovery.app.data.datasource.model.ChatInputDto
import com.dishcovery.app.data.datasource.model.MessageCreateRequest
import com.dishcovery.app.data.mapper.ChatMapper
import com.dishcovery.app.data.mapper.MessageMapper
import com.dishcovery.app.domain.model.Chat
import com.dishcovery.app.domain.model.Message
import com.dishcovery.app.domain.repository.ChatRepository
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val dataSource: ChatDataSource,
    private val chatMapper: ChatMapper,
    private val messageMapper: MessageMapper
) : ChatRepository {

    private val cachedMessagesByChatId = mutableMapOf<Long, MutableList<Message>>()

    override suspend fun getChats(userId: Long): Result<List<Chat>> = try {
        val dtos = dataSource.getChatsByUserId(userId)
        val chats = dtos.map { chatMapper.toDomain(it) }
        Result.success(chats)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getMessages(chatId: Long): Result<List<Message>> = try {
        val dtos = dataSource.getMessages(chatId)
        val messages = dtos.map { messageMapper.toDomain(it) }
        cachedMessagesByChatId[chatId] = messages.toMutableList()
        Result.success(messages)
    } catch (e: Exception) {
        Result.failure(mapToUserFriendlyException(e))
    }

    override suspend fun sendMessage(chatId: Long, senderId: Long, text: String): Result<Message> = try {
        val request = MessageCreateRequest(chatId, senderId, text)
        val dto = dataSource.sendMessage(request)
        val domainMessage = messageMapper.toDomain(dto)
        val messages = cachedMessagesByChatId.getOrPut(chatId) { mutableListOf() }
        messages.add(domainMessage)
        Result.success(domainMessage)
    } catch (e: Exception) {
        Result.failure(mapToUserFriendlyException(e))
    }

    override suspend fun updateMessage(id: Long, text: String): Result<Message> = Result.failure(Exception("Not implemented"))

    override suspend fun createChat(
        senderId: Long,
        receiverId: Long,
        name: String,
        avatarUrl: String,
        planId: Long?
    ): Result<Chat> = try {
        val input = ChatInputDto(
            senderId = senderId,
            receiverId = receiverId,
            name = name,
            avatarUrl = avatarUrl,
            planId = planId,
            lastMessage = "¡Nuevo plan de intercambio creado!",
            time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            unreadCount = 0
        )
        val dto = dataSource.createChat(input)
        Result.success(chatMapper.toDomain(dto))
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun mapToUserFriendlyException(exception: Exception): Exception {
        return when (exception) {
            is IOException -> Exception("Error de conexión con el servidor", exception)
            else -> Exception(exception.message ?: "Error inesperado en el chat", exception)
        }
    }
}
