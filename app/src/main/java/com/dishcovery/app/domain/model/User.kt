package com.dishcovery.app.domain.model

/**
 * Represents a user in the system
 */
data class User(
    val id: Long,
    val username: String,
    val password: String,
    val role: UserRole
)

/**
 * Represents the role of a user within the system.
 */
sealed class UserRole {
    object User : UserRole()
    /** Administrator with a dedicated view and no bottom navigation bar. */
    object Admin : UserRole()
}