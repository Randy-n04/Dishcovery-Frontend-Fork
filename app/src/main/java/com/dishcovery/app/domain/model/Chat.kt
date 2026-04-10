package com.dishcovery.app.domain.model

data class Chat(
    val id: Long,
    val userId1: Long,
    val userId2: Long,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int,
    val avatarUrl: String
)