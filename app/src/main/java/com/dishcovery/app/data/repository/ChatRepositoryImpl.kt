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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val dataSource: ChatDataSource,
    private val chatMapper: ChatMapper,
    private val messageMapper: MessageMapper
) : ChatRepository {

    // Caché en memoria para persistencia durante la sesión
    private val cachedMessagesByChatId = mutableMapOf<Long, MutableList<Message>>()

    override suspend fun getChats(userId: Long): Result<List<Chat>> = try {
        val dtos = dataSource.getChats(userId)
        val chats = dtos.map { dto ->
            val domainChat = chatMapper.toDomain(dto)
            // Ya no llamamos a getMessages aquí para evitar problemas de suspensión y red excesiva.
            // El backend real ya provee el lastMessage en el DTO del chat.
            domainChat
        }
        Result.success(chats)
    } catch (e: Exception) {
        Result.failure(mapToUserFriendlyException(e))
    }

    override suspend fun getMessages(chatId: Long): Result<List<Message>> = try {
        val dtos = dataSource.getMessages(chatId)
        val messages = dtos.map { messageMapper.toDomain(it) }

        // Actualizamos caché
        cachedMessagesByChatId[chatId] = messages.toMutableList()

        Result.success(messages)
    } catch (e: Exception) {
        Result.failure(mapToUserFriendlyException(e))
    }

    override suspend fun sendMessage(chatId: Long, senderId: Long, text: String): Result<Message> = try {
        // Usamos el campo 'message' que es el que espera tu DTO real
        val request = MessageCreateRequest(chatId, senderId, text)
        val dto = dataSource.sendMessage(request)
        val domainMessage = messageMapper.toDomain(dto)

        // Agregar a caché local para actualización instantánea
        val messages = cachedMessagesByChatId.getOrPut(chatId) { mutableListOf() }
        messages.add(domainMessage)

        Result.success(domainMessage)
    } catch (e: Exception) {
        Result.failure(mapToUserFriendlyException(e))
    }

    override suspend fun updateMessage(id: Long, text: String): Result<Message> = try {
        val request = MessageUpdateRequest(text)
        val dto = dataSource.updateMessage(id, request)
        val domainMessage = messageMapper.toDomain(dto)

        // Actualizar en caché si existe
        cachedMessagesByChatId.forEach { (_, list) ->
            val index = list.indexOfFirst { it.id == id }
            if (index != -1) {
                list[index] = domainMessage
            }
        }

        Result.success(domainMessage)
    } catch (e: Exception) {
        Result.failure(mapToUserFriendlyException(e))
    }

    private fun mapToUserFriendlyException(exception: Exception): Exception {
        return when (exception) {
            is IOException -> Exception("Error de conexión con el servidor", exception)
            else -> Exception(exception.message ?: "Error inesperado en el chat", exception)
        }
    }
}