package com.dishcovery.app.data.datasource.model

/**
 * DTO for User in data layer
 */
data class UserDto(
    val id: Long,
    val username: String,
    val password: String,
    val role: String
)