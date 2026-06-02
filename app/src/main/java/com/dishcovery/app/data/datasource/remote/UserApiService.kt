package com.dishcovery.app.data.datasource.remote

import com.dishcovery.app.data.datasource.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Retrofit interface for User-related API endpoints
 */
interface UserApiService {
    
    @POST("v1/users/login")
    suspend fun login(@Body loginInput: UserLoginInputDto): Response<LoginResponseDto>

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

    @DELETE("users/{id}")
    suspend fun deleteById(@Path("id") id: Long): Response<Unit>
}
