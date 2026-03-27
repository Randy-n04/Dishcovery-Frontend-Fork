package com.dishcovery.app.domain.repository

import com.dishcovery.app.domain.model.Chat

interface ChatRepository {
    suspend fun getChats(userId: Long): Result<List<Chat>>
}