package com.dishcovery.app.domain.model

/**
 * Represents a user in the system
 */
data class User(
    val id: Long,
    val username: String,
    val password: String
)