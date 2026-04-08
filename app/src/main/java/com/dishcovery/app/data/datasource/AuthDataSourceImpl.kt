package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.UserDto
import javax.inject.Inject

/**
 * Implementation of AuthDataSource
 */
class AuthDataSourceImpl @Inject constructor() : AuthDataSource {

    override suspend fun login(username: String, password: String): UserDto? {
        return AuthProvider.login(username, password)
    }
}
