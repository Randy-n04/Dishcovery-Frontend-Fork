package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.ChatDto
import com.dishcovery.app.data.datasource.model.MessageDto
import kotlinx.coroutines.flow.Flow

interface ChatDataSource {

    fun getChats(userId: Long): Flow<List<ChatDto>>


    fun getMessages(chatId: Long): List<MessageDto>


    fun sendMessage(chatId: Long, senderId: Long, text: String)
}