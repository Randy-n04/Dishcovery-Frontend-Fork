package com.dishcovery.app.data.datasource.model

data class MessageCreateRequest(
    val chatId: Long,
    val senderId: Long,
    val message: String
)

data class MessageUpdateRequest(
    val message: String
)