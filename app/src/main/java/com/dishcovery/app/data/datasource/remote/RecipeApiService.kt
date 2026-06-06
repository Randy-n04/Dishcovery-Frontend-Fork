package com.dishcovery.app.data.datasource.remote

import com.dishcovery.app.data.datasource.model.FeedAiRequestDto
import com.dishcovery.app.data.datasource.model.FeedAiResponseDto
import com.dishcovery.app.data.datasource.model.KitchenCategoryResultDto
import com.dishcovery.app.data.datasource.model.RecipeResultDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RecipeApiService {

    @GET("v1/recipes")
    suspend fun getRecipes(): Response<List<RecipeResultDto>>

    @GET("v1/kitchen-categories")
    suspend fun getKitchenCategories(): Response<List<KitchenCategoryResultDto>>

    @POST("v1/feed/ai")
    suspend fun getAiFeed(@Body request: FeedAiRequestDto): Response<FeedAiResponseDto>
}

