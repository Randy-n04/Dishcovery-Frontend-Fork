package com.dishcovery.app.data.mapper

import com.dishcovery.app.data.datasource.model.ChatDto
import com.dishcovery.app.domain.model.Chat
import javax.inject.Inject

class ChatMapper @Inject constructor() {

    fun toDto(chat: Chat): ChatDto {
        return ChatDto(
            id = chat.id,
            userId1 = chat.userId1,
            userId2 = chat.userId2,
            name = chat.name,
            lastMessage = chat.lastMessage,
            time = chat.time,
            unread = chat.unreadCount,
            avatarUrl = chat.avatarUrl
        )
    }

    fun toDomain(dto: ChatDto): Chat {
        return Chat(
            id = dto.id,
            userId1 = dto.userId1,
            userId2 = dto.userId2,
            name = dto.name,
            lastMessage = dto.lastMessage,
            time = dto.time,
            unreadCount = dto.unread,
            avatarUrl = dto.avatarUrl ?: ""
        )
    }
}
