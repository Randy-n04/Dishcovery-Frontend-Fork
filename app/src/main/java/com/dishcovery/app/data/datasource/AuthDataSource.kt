package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.UserDto

/**
 * DataSource interface for authentication
 */
interface AuthDataSource {
    suspend fun login(username: String, password: String): UserDto?
}