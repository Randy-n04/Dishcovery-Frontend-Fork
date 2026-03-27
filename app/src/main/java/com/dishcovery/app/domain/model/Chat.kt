package com.dishcovery.app.domain.model

data class Chat(
    val id: Long,
    val userId: Long,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int
)