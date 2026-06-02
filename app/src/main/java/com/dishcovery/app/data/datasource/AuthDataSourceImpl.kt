package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.LoginResponseDto
import com.dishcovery.app.data.datasource.model.UserLoginInputDto
import com.dishcovery.app.data.datasource.remote.UserApiService
import retrofit2.Response
import javax.inject.Inject

/**
 * Implementation of AuthDataSource connected to a real backend
 */
class AuthDataSourceImpl @Inject constructor(
    private val apiService: UserApiService
) : AuthDataSource {

    override suspend fun login(loginInput: UserLoginInputDto): Response<LoginResponseDto> {
        return apiService.login(loginInput)
    }
}
