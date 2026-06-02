package com.dishcovery.app.domain.repository

import com.dishcovery.app.domain.model.User
import com.dishcovery.app.data.datasource.model.UserInputDto

/**
 * Repository interface for User-related operations
 */
interface UserRepository {
    suspend fun findAll(): Result<List<User>>
    suspend fun findById(id: Long): Result<User>
    suspend fun create(userInput: UserInputDto): Result<User>
    suspend fun update(id: Long, userInput: UserInputDto): Result<User>
    suspend fun deleteById(id: Long): Result<Unit>
}
