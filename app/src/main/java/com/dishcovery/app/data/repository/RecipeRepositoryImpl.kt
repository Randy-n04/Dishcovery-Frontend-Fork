package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.RecipeProvider
import com.dishcovery.app.data.mapper.RecipeMapper
import com.dishcovery.app.domain.model.Recipe
import com.dishcovery.app.domain.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val mapper: RecipeMapper
) : RecipeRepository {

    // Simulated local storage for saved recipes
    private val savedRecipeIds = mutableSetOf<Long>()

    override suspend fun getPopularRecipes(): Result<List<Recipe>> = withContext(Dispatchers.IO) {
        try {
            val dtos = RecipeProvider.getAll()
            val recipes = dtos.map { mapper.mapToDomain(it) }
            Result.success(recipes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSavedRecipes(): Result<List<Recipe>> = withContext(Dispatchers.IO) {
        try {
            val dtos = RecipeProvider.getAll().filter { it.id in savedRecipeIds }
            val recipes = dtos.map { mapper.mapToDomain(it) }
            Result.success(recipes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveRecipe(recipe: Recipe): Result<Unit> = withContext(Dispatchers.IO) {
        savedRecipeIds.add(recipe.id)
        Result.success(Unit)
    }

    override suspend fun removeSavedRecipe(recipeId: Long): Result<Unit> = withContext(Dispatchers.IO) {
        savedRecipeIds.remove(recipeId)
        Result.success(Unit)
    }
}
