package com.dishcovery.app.domain.model

data class Message(
    val id: Long,
    val chatId: Long,
    val senderId: Long,
    val message: String,
    val time: String,
    val edited: Boolean = false
)