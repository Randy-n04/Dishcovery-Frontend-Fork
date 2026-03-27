package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.ChatDto
import kotlinx.coroutines.flow.Flow

interface ChatDataSource {
    suspend fun getChats(userId: Long): Flow<List<ChatDto>>
}