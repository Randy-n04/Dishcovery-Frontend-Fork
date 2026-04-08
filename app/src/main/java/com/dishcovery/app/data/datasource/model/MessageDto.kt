package com.dishcovery.app.data.datasource.model

data class MessageDto(
    val id: Long,
    val chatId: Long,
    val senderId: Long,
    val message: String,
    val time: String,
    val edited: Boolean = false
)