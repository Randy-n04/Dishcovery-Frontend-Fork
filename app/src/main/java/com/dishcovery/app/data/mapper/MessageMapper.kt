package com.dishcovery.app.data.mapper

import com.dishcovery.app.data.datasource.model.MessageDto
import com.dishcovery.app.domain.model.Message
import javax.inject.Inject

class MessageMapper @Inject constructor() {
    fun toDomain(dto: MessageDto): Message {
        return Message(
            id = dto.id,
            chatId = dto.chatId,
            senderId = dto.senderId,
            message = dto.message,
            time = dto.time,
            edited = dto.edited
        )
    }

    fun toDomainList(dtos: List<MessageDto>): List<Message> {
        return dtos.map { toDomain(it) }
    }
}
