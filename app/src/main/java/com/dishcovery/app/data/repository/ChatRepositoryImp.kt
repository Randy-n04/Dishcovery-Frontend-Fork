package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.ChatDataSource
import com.dishcovery.app.data.mapper.ChatMapper
import com.dishcovery.app.domain.model.Chat
import com.dishcovery.app.domain.repository.ChatRepository
import kotlinx.coroutines.flow.first

class ChatRepositoryImpl(
    private val dataSource: ChatDataSource,
    private val mapper: ChatMapper
) : ChatRepository {

    override suspend fun getChats(userId: Long): Result<List<Chat>> = try {
        val chats = dataSource.getChats(userId).first().map {
            mapper.toDomain(it)
        }
        Result.success(chats)
    } catch (e: Exception) {
        Result.failure(e)
    }
}