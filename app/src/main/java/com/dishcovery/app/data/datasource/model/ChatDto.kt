package com.dishcovery.app.data.datasource.model

import com.google.gson.annotations.SerializedName

data class ChatDto(
    val id: Long,
    val userId: Long,
    val name: String,
    val lastMessage: String,
    val time: String,
    @SerializedName("unread")
    val unreadCount: Int,
    @SerializedName("avatarUrl")
    val avatarUrl: String? = null
)