package com.dishcovery.app.data.datasource.model

data class ChatDto(
    val id: Long,
    val userId: Long,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int,
    val avatarUrl: String
)