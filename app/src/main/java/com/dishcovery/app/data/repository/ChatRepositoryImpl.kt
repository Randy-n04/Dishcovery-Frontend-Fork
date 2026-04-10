package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.ChatDataSource
import com.dishcovery.app.data.datasource.model.MessageCreateRequest
import com.dishcovery.app.data.datasource.model.MessageUpdateRequest
import com.dishcovery.app.data.mapper.ChatMapper
import com.dishcovery.app.data.mapper.MessageMapper
import com.dishcovery.app.domain.model.Chat
import com.dishcovery.app.domain.model.Message
import com.dishcovery.app.domain.repository.ChatRepository
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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
        val chats = dataSource.getChats(userId).map { dto ->
            val domainChat = chatMapper.toDomain(dto)
            
            // Si no tenemos mensajes en caché para este chat, intentamos traerlos
            // para que el "lastMessage" esté actualizado desde el inicio.
            if (!cachedMessagesByChatId.containsKey(domainChat.id)) {
                getMessages(domainChat.id)
            }
            
            mergeChatPreview(domainChat)
        }
        Result.success(chats)
    } catch (e: Exception) {
        Result.failure(mapToUserFriendlyException(e))
    }

    override suspend fun getMessages(chatId: Long): Result<List<Message>> = try {
        val messages = dataSource.getMessages(chatId)
            .filter { it.chatId == chatId }
            .map {
                messageMapper.mapToDomain(it)
            }
        cacheMessages(chatId, messages)
        Result.success(messages)
    } catch (e: Exception) {
        Result.failure(mapToUserFriendlyException(e))
    }

    override suspend fun sendMessage(chatId: Long, senderId: Long, text: String): Result<Message> = try {
        val request = MessageCreateRequest(chatId, senderId, text)
        val dto = dataSource.sendMessage(request)
        val domainMessage = messageMapper.mapToDomain(dto)
        cacheMessage(domainMessage)
        Result.success(domainMessage)
    } catch (e: Exception) {
        Result.failure(mapToUserFriendlyException(e))
    }

    override suspend fun updateMessage(id: Long, text: String): Result<Message> = try {
        val request = MessageUpdateRequest(text)
        val dto = dataSource.updateMessage(id, request)
        val domainMessage = messageMapper.mapToDomain(dto)
        cacheMessage(domainMessage)
        Result.success(domainMessage)
    } catch (e: Exception) {
        Result.failure(mapToUserFriendlyException(e))
    }

    private fun mergeChatPreview(chat: Chat): Chat {
        val latestMessage = cachedMessagesByChatId[chat.id]?.lastOrNull() ?: return chat
        return chat.copy(
            lastMessage = latestMessage.message,
            time = latestMessage.time
        )
    }

    private fun cacheMessages(chatId: Long, messages: List<Message>) {
        cachedMessagesByChatId[chatId] = messages.toMutableList()
    }

    private fun cacheMessage(message: Message) {
        val messages = cachedMessagesByChatId.getOrPut(message.chatId) { mutableListOf() }
        val index = messages.indexOfFirst { it.id == message.id }
        if (index >= 0) {
            messages[index] = message
        } else {
            messages.add(message)
        }
    }

    private fun mapToUserFriendlyException(exception: Exception): Exception {
        val message = when (exception) {
            is UnknownHostException,
            is SocketTimeoutException -> "Sin conexion. Verifica internet o que Mockoon este encendido."
            is IOException -> "Error de red. Intenta nuevamente."
            else -> exception.message ?: "Ocurrio un error inesperado."
        }
        return Exception(message, exception)
    }
}