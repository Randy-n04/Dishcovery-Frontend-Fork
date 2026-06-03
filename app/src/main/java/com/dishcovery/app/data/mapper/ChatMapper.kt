package com.dishcovery.app.data.mapper

import com.dishcovery.app.data.datasource.model.ChatDto
import com.dishcovery.app.domain.model.Chat
import javax.inject.Inject

class ChatMapper @Inject constructor() {

    fun toDto(chat: Chat): ChatDto {
        return ChatDto(
            id = chat.id,
            senderId = chat.senderId,
            receiverId = chat.receiverId,
            senderName = chat.senderName,
            receiverName = chat.receiverName,
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
            senderId = dto.senderId,
            receiverId = dto.receiverId,
            senderName = dto.senderName,
            receiverName = dto.receiverName,
            name = dto.name,
            lastMessage = dto.lastMessage,
            time = dto.time,
            unreadCount = dto.unreadCount,
            avatarUrl = dto.avatarUrl
        )
    }
}
