package com.dishcovery.app.data.mapper

import com.dishcovery.app.data.datasource.model.ChatDto
import com.dishcovery.app.domain.model.Chat
import javax.inject.Inject

class ChatMapper @Inject constructor() {

    fun toDto(chat: Chat): ChatDto {
        return ChatDto(
            id = chat.id,
            userId = chat.userId,
            name = chat.name,
            lastMessage = chat.lastMessage,
            time = chat.time,
            unreadCount = chat.unreadCount,
            avatarUrl = chat.avatarUrl
        )
    }

    fun toDomain(dto: ChatDto): Chat {
        return Chat(
            id = dto.id,
            userId = dto.userId,
            name = dto.name,
            lastMessage = dto.lastMessage,
            time = dto.time,
            unreadCount = dto.unreadCount,
            avatarUrl = dto.avatarUrl
        )
    }
}
