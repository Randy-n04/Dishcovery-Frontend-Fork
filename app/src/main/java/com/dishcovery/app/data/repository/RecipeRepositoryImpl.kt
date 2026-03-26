package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.RecipeProvider
import com.dishcovery.app.data.mapper.RecipeMapper
import com.dishcovery.app.domain.model.Recipe
import com.dishcovery.app.domain.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepositoryImpl(
    private val mapper: RecipeMapper = RecipeMapper()
) : RecipeRepository {

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
            val dtos = RecipeProvider.getAll().take(3)
            val recipes = dtos.map { mapper.mapToDomain(it) }
            Result.success(recipes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
