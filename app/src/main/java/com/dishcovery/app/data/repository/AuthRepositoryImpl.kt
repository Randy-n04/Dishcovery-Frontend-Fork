package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.AuthDataSource
import com.dishcovery.app.data.mapper.UserMapper
import com.dishcovery.app.domain.model.User
import com.dishcovery.app.domain.repository.AuthRepository

/**
 * Implementation of AuthRepository
 */
class AuthRepositoryImpl(
    private val dataSource: AuthDataSource,
    private val mapper: UserMapper
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<User> {
        return try {
            val userDto = dataSource.login(username, password)

            if (userDto != null) {
                val user = mapper.mapToDomain(userDto)
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid credentials"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}