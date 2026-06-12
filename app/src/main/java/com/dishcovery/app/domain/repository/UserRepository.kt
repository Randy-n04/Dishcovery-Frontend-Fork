package com.dishcovery.app.domain.repository

import com.dishcovery.app.data.datasource.model.PageResultDto
import com.dishcovery.app.domain.model.User
import com.dishcovery.app.data.datasource.model.UserInputDto
import com.dishcovery.app.data.datasource.model.AccountUpdateDto

/**
 * Repository interface for User-related operations
 */
interface UserRepository {
    suspend fun findAll(): Result<List<User>>
    suspend fun findById(id: Long): Result<User>
    suspend fun create(userInput: UserInputDto): Result<User>
    suspend fun update(id: Long, userInput: UserInputDto): Result<User>
    suspend fun deleteById(id: Long): Result<Unit>

    suspend fun findAllPaginated(page: Int, size: Int): Result<PageResultDto<User>>
    suspend fun updateAccount(
        id: Long,
        request: AccountUpdateDto
    ): Result<User>

    suspend fun blockUser(id: Long): Result<User>
}
