package com.dishcovery.app.data.datasource.model

data class AccountUpdateDto(
    val username: String?,
    val currentPassword: String,
    val newPassword: String?
)