package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.ChatDataSource
import com.dishcovery.app.data.datasource.model.MessageCreateRequest
import com.dishcovery.app.data.datasource.model.MessageUpdateRequest
import com.dishcovery.app.data.mapper.ChatMapper
import com.dishcovery.app.data.mapper.MessageMapper
import com.dishcovery.app.domain.model.Chat
import com.dishcovery.app.domain.model.Message
import com.dishcovery.app.domain.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val dataSource: ChatDataSource,
    private val chatMapper: ChatMapper,
    private val messageMapper: MessageMapper
) : ChatRepository {

    override suspend fun getChats(): Result<List<Chat>> = try {
        val chats = dataSource.getChats().map {
            chatMapper.toDomain(it)
        }
        Result.success(chats)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getMessages(chatId: Long): Result<List<Message>> = try {
        val messages = dataSource.getMessages(chatId).map {
            messageMapper.mapToDomain(it)
        }
        Result.success(messages)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun sendMessage(chatId: Long, senderId: Long, text: String): Result<Message> = try {
        val request = MessageCreateRequest(chatId, senderId, text)
        val dto = dataSource.sendMessage(request)
        Result.success(messageMapper.mapToDomain(dto))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateMessage(id: Long, text: String): Result<Message> = try {
        val request = MessageUpdateRequest(text)
        val dto = dataSource.updateMessage(id, request)
        Result.success(messageMapper.mapToDomain(dto))
    } catch (e: Exception) {
        Result.failure(e)
    }
}