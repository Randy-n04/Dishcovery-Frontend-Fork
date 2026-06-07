package com.dishcovery.app.domain.repository

import com.dishcovery.app.domain.model.FeedAiResult
import com.dishcovery.app.domain.model.KitchenCategory
import com.dishcovery.app.domain.model.Recipe

interface RecipeRepository {
    suspend fun getPopularRecipes(): Result<List<Recipe>>
    suspend fun getKitchenCategories(): Result<List<KitchenCategory>>

    suspend fun getAiFeed(
        categoryId: Long,
        preference: String?,
        ingredients: List<String>?
    ): Result<FeedAiResult>

    suspend fun getSavedRecipes(profileId: Long): Result<List<Recipe>>
    suspend fun saveRecipe(profileId: Long, recipeId: Long): Result<Unit>
    suspend fun removeSavedRecipe(profileId: Long, recipeId: Long): Result<Unit>
}