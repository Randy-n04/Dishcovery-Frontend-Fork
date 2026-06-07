package com.dishcovery.app.data.datasource.remote

import com.dishcovery.app.data.datasource.model.*
import retrofit2.Response
import retrofit2.http.*
import com.dishcovery.app.data.datasource.model.AccountUpdateDto
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path
/**
 * Retrofit interface for User-related API endpoints
 */
interface UserApiService {
    
    @POST("v1/users/login")
    suspend fun login(@Body loginInput: UserLoginInputDto): Response<LoginResponseDto>

    @POST("v1/users/logoutUser")
    suspend fun logout(): Response<Unit>

    @GET("users")
    suspend fun findAll(): Response<List<UserResultDto>>

    @GET("users/{id}")
    suspend fun findById(@Path("id") id: Long): Response<UserResultDto>

    @POST("users")
    suspend fun create(@Body userInput: UserInputDto): Response<UserResultDto>

    @PUT("users/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body userInput: UserInputDto
    ): Response<UserResultDto>


    @PUT("v1/users/{id}/account")
    suspend fun updateAccount(
        @Path("id") id: Long,
        @Body request: AccountUpdateDto
    ): Response<UserResultDto>

    @DELETE("users/{id}")
    suspend fun deleteById(@Path("id") id: Long): Response<Unit>
}
