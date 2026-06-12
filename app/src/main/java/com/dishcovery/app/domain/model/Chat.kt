package com.dishcovery.app.domain.model

data class Chat(
    val id: Long,
    val senderId: Long,
    val receiverId: Long,
    val senderName: String?,
    val receiverName: String?,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int,
    val avatarUrl: String,
    val planId: Long? = null // Vinculación con el plan de intercambio
)
