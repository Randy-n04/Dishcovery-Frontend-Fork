package com.dishcovery.app.data.datasource.model

import com.google.gson.annotations.SerializedName

data class ChatDto(
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
    val planId: Long? = null
)

data class ChatInputDto(
    val id: Long? = null,
    val senderId: Long? = null,
    val receiverId: Long? = null,
    val name: String? = null,
    val lastMessage: String? = null,
    val time: String? = null,
    val unreadCount: Int? = null,
    val avatarUrl: String? = null,
    val planId: Long? = null
)
