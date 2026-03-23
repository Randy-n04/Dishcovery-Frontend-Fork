package com.dishcovery.app.domain.repository

import com.dishcovery.app.domain.model.User

/**
 * Repository for authentication operations
 */
interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
}