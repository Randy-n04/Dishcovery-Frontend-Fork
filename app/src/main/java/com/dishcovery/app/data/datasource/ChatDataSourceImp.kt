package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.ChatDto
import com.dishcovery.app.data.mapper.ChatMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ChatDataSourceImpl(
    private val mapper: ChatMapper
) : ChatDataSource {

    override suspend fun getChats(userId: Long): Flow<List<ChatDto>> = flow {
        val chats = ChatProvider.getChats()
            .filter { it.userId == userId }

        emit(chats.map { mapper.toDto(it) })
    }
}