package com.dishcovery.app.data.datasource.model

data class ChatDto(
    val id: Long,
    val userId1: Long,
    val userId2: Long,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unread: Int,
    val avatarUrl: String? = null
)