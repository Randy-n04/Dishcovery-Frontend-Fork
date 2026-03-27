package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.ChatDto
import com.dishcovery.app.data.datasource.model.MessageDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ChatDataSourceImpl(
    private val mapper: com.dishcovery.app.data.mapper.ChatMapper
) : ChatDataSource {

    override fun getChats(userId: Long): Flow<List<ChatDto>> = flow {
        val chats = ChatProvider.getChats(userId)
        emit(chats.map { mapper.toDto(it) })
    }

    override fun getMessages(chatId: Long): List<MessageDto> {
        return ChatProvider.getMessages(chatId).map {
            MessageDto(
                id = it.id,
                chatId = it.chatId,
                senderId = it.senderId,
                message = it.message,
                time = it.time
            )
        }
    }

    override fun sendMessage(chatId: Long, senderId: Long, text: String) {
        ChatProvider.sendMessage(chatId, senderId, text)
    }
}