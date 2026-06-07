package com.dishcovery.app.data.datasource.remote

import com.dishcovery.app.data.datasource.model.ExchangePlanInputDto
import com.dishcovery.app.data.datasource.model.ExchangePlanResultDto
import retrofit2.http.*

interface ExchangePlanApiService {

    @GET("v1/exchange-plans")
    suspend fun findAll(): List<ExchangePlanResultDto>

    @GET("v1/exchange-plans/{id}")
    suspend fun findById(@Path("id") id: Long): ExchangePlanResultDto

    @GET("v1/exchange-plans/chat/{chatId}")
    suspend fun findByChatId(@Path("chatId") chatId: Long): ExchangePlanResultDto

    @POST("v1/exchange-plans")
    suspend fun create(@Body input: ExchangePlanInputDto): ExchangePlanResultDto

    @PUT("v1/exchange-plans/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body input: ExchangePlanInputDto
    ): ExchangePlanResultDto

    @DELETE("v1/exchange-plans/{id}")
    suspend fun deleteById(@Path("id") id: Long)
}