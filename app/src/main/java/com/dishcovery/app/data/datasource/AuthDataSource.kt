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
     * Se devuelve un objeto [Response] para poder extraer el JWT de los headers.
     */
    suspend fun login(loginInput: UserLoginInputDto): Response<LoginResponseDto>
}
