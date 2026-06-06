package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.model.UserLoginInputDto
import com.dishcovery.app.data.datasource.remote.UserApiService
import com.dishcovery.app.data.local.TokenManager
import com.dishcovery.app.data.mapper.UserMapper
import com.dishcovery.app.domain.model.User
import com.dishcovery.app.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: UserApiService,
    private val mapper: UserMapper,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<User> {
        return try {
            val loginInput = UserLoginInputDto(username, password)
            val response = apiService.login(loginInput)
            
            if (response.isSuccessful && response.body() != null) {
                // 1. Extraer el Token del Header (Authorization: Bearer <token>)
                val authHeader = response.headers()["Authorization"]
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    val token = authHeader.substring(7)
                    // 2. Guardar el token de forma segura en EncryptedSharedPreferences
                    tokenManager.saveToken(token)
                }

                // 3. Mapear la respuesta (UserDetails) al modelo de dominio User
                Result.success(mapper.toDomainFromLogin(response.body()!!))
            } else {
                Result.failure(Exception("Credenciales inválidas o error de servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            val response = apiService.logout()
            if (response.isSuccessful) {
                tokenManager.deleteToken()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al cerrar sesión: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Incluso si falla la red, borramos el token localmente
            tokenManager.deleteToken()
            Result.failure(e)
        }
    }
}
