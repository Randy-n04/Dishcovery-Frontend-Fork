package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.UserDto

/**
 * Implementation of AuthDataSource
 */
class AuthDataSourceImpl : AuthDataSource {

    override suspend fun login(username: String, password: String): UserDto? {
        return AuthProvider.login(username, password)
    }
}