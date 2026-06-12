package com.dishcovery.app.data.repository

import android.util.Log
import com.dishcovery.app.data.datasource.model.PageResultDto
import com.dishcovery.app.data.datasource.model.UserInputDto
import com.dishcovery.app.data.datasource.remote.UserApiService
import com.dishcovery.app.data.mapper.UserMapper
import com.dishcovery.app.domain.model.User
import com.dishcovery.app.domain.repository.UserRepository
import javax.inject.Inject
import com.dishcovery.app.data.datasource.model.AccountUpdateDto

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

    override suspend fun findAllPaginated(page: Int, size: Int): Result<PageResultDto<User>> {
        return try {
            val response = apiService.findAllPaginated(page, size)
            if (response.isSuccessful && response.body() != null) {
                val page = response.body()!!
                Result.success(
                    PageResultDto(
                        content       = page.content.map { mapper.toDomain(it) },
                        totalElements = page.totalElements,
                        totalPages    = page.totalPages,
                        number        = page.number
                    )
                )
            } else {
                Result.failure(Exception("Error al obtener usuarios: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun blockUser(id: Long): Result<User> {
        return try {
            val response = apiService.blockUser(id)
            Log.d("BlockUser", "Code: ${response.code()}")
            Log.d("BlockUser", "Body: ${response.body()}")
            Log.d("BlockUser", "isBlocked en body: ${response.body()?.blocked}")
            if (response.isSuccessful && response.body() != null) {
                Result.success(mapper.toDomain(response.body()!!))
            } else {
                Result.failure(Exception("Error al bloquear usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateAccount(
        id: Long,
        request: AccountUpdateDto
    ): Result<User> {
        return try {
            val response = apiService.updateAccount(id, request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(mapper.toDomain(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string().orEmpty()

                val message = when {
                    errorBody.contains("Username already exists", ignoreCase = true) ->
                        "Ese nombre de usuario ya existe. Intenta con otro."

                    errorBody.contains("Current password is incorrect", ignoreCase = true) ->
                        "La contraseña actual es incorrecta."

                    else ->
                        "No se pudo actualizar la cuenta."
                }

                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(Exception("No se pudo conectar con el servidor. Intenta nuevamente."))
        }
    }
}
