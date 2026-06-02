package com.dishcovery.app.domain.model

/**
 * Represents a user in the system (Result from Backend)
 */
data class User(
    val id: Long,
    val username: String,
    val role: UserRole
)

/**
 * Represents the role of a user within the system.
 */
sealed class UserRole(val id: Long, val name: String) {
    class Client(id: Long, name: String = "USER") : UserRole(id, name)
    class Admin(id: Long, name: String = "ADMIN") : UserRole(id, name)
}
