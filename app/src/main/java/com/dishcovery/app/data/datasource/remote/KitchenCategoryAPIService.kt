package com.dishcovery.app.data.datasource.remote

import com.dishcovery.app.data.datasource.model.KitchenCategoryDetailsDto
import com.dishcovery.app.data.datasource.model.KitchenCategoryDto
import com.dishcovery.app.data.datasource.model.KitchenCategoryResultDto
import com.dishcovery.app.data.datasource.model.TrendingRequest
import retrofit2.http.*


interface KitchenCategoryApiService {
    @GET("v1/kitchen-categories")
    suspend fun getAll(): List<KitchenCategoryDto>

    @GET("v1/kitchen-categories/trending")
    suspend fun getTrending(): List<KitchenCategoryDto>

    @POST("v1/kitchen-categories")
    suspend fun create(@Body body: KitchenCategoryDetailsDto): KitchenCategoryResultDto

    @PATCH("v1/kitchen-categories/{id}/trending")
    suspend fun setTrending(
        @Path("id") id: Long,
        @Body body: TrendingRequest
    ): KitchenCategoryResultDto
}