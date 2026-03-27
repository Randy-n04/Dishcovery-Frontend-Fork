package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.ChatDataSource
import com.dishcovery.app.data.mapper.ChatMapper
import com.dishcovery.app.data.mapper.MessageMapper
import com.dishcovery.app.domain.model.Chat
import com.dishcovery.app.domain.model.Message
import com.dishcovery.app.domain.repository.ChatRepository
import kotlinx.coroutines.flow.first

class ChatRepositoryImpl(
    private val dataSource: ChatDataSource,
    private val chatMapper: ChatMapper,
    private val messageMapper: MessageMapper
) : ChatRepository {

    override suspend fun getChats(userId: Long): Result<List<Chat>> = try {
        val chats = dataSource.getChats(userId).first().map {
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

    override suspend fun sendMessage(chatId: Long, senderId: Long, text: String) {
        dataSource.sendMessage(chatId, senderId, text)
    }
}
