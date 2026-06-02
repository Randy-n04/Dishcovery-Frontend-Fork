package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.model.UserInputDto
import com.dishcovery.app.data.datasource.remote.UserApiService
import com.dishcovery.app.data.mapper.UserMapper
import com.dishcovery.app.domain.model.User
import com.dishcovery.app.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: UserApiService,
    private val mapper: UserMapper
) : UserRepository {

    override suspend fun findAll(): Result<List<User>> {
        return try {
            val response = apiService.findAll()
            if (response.isSuccessful && response.body() != null) {
                Result.success(mapper.toDomainList(response.body()!!))
            } else {
                Result.failure(Exception("Error al obtener usuarios: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun findById(id: Long): Result<User> {
        return try {
            val response = apiService.findById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(mapper.toDomain(response.body()!!))
            } else {
                Result.failure(Exception("Usuario no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun create(userInput: UserInputDto): Result<User> {
        return try {
            val response = apiService.create(userInput)
            if (response.isSuccessful && response.body() != null) {
                Result.success(mapper.toDomain(response.body()!!))
            } else {
                Result.failure(Exception("Error al crear usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun update(id: Long, userInput: UserInputDto): Result<User> {
        return try {
            val response = apiService.update(id, userInput)
            if (response.isSuccessful && response.body() != null) {
                Result.success(mapper.toDomain(response.body()!!))
            } else {
                Result.failure(Exception("Error al actualizar usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteById(id: Long): Result<Unit> {
        return try {
            val response = apiService.deleteById(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
