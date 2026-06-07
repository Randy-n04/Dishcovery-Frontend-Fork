package com.dishcovery.app.data.datasource.remote

import com.dishcovery.app.data.datasource.model.FeedAiRequestDto
import com.dishcovery.app.data.datasource.model.FeedAiResponseDto
import com.dishcovery.app.data.datasource.model.KitchenCategoryResultDto
import com.dishcovery.app.data.datasource.model.RecipeResultDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.DELETE

interface RecipeApiService {

    @GET("v1/recipes")
    suspend fun getRecipes(): Response<List<RecipeResultDto>>

    @GET("v1/kitchen-categories")
    suspend fun getKitchenCategories(): Response<List<KitchenCategoryResultDto>>

    @POST("v1/feed/ai")
    suspend fun getAiFeed(@Body request: FeedAiRequestDto): Response<FeedAiResponseDto>


    @GET("v1/recipes/popular")
    suspend fun getPopularRecipes(): Response<List<RecipeResultDto>>

    @GET("v1/profiles/{profileId}/saved-recipes")
    suspend fun getSavedRecipes(
        @Path("profileId") profileId: Long
    ): Response<List<RecipeResultDto>>

    @POST("v1/profiles/{profileId}/saved-recipes/{recipeId}")
    suspend fun saveRecipe(
        @Path("profileId") profileId: Long,
        @Path("recipeId") recipeId: Long
    ): Response<Unit>

    @DELETE("v1/profiles/{profileId}/saved-recipes/{recipeId}")
    suspend fun removeSavedRecipe(
        @Path("profileId") profileId: Long,
        @Path("recipeId") recipeId: Long
    ): Response<Unit>



}

