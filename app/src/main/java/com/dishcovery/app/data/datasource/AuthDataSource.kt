package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.LoginResponseDto
import com.dishcovery.app.data.datasource.model.UserLoginInputDto
import retrofit2.Response

/**
 * DataSource interface for authentication connected to a real backend
 */
interface AuthDataSource {
    /**
     * Realiza la petición de login al servidor.
     */
    suspend fun login(loginInput: UserLoginInputDto): Response<LoginResponseDto>

    /**
     * Realiza la petición de logout al servidor.
     */
    suspend fun logout(): Response<Unit>
}
