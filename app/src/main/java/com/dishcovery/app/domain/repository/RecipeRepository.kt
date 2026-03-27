package com.dishcovery.app.domain.repository

import com.dishcovery.app.domain.model.Recipe

interface RecipeRepository {
    suspend fun getPopularRecipes(): Result<List<Recipe>>
    suspend fun getSavedRecipes(): Result<List<Recipe>>
    suspend fun saveRecipe(recipe: Recipe): Result<Unit>
    suspend fun removeSavedRecipe(recipeId: Long): Result<Unit>
}
