package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.model.FeedAiRequestDto
import com.dishcovery.app.data.datasource.remote.RecipeApiService
import com.dishcovery.app.data.mapper.RecipeMapper
import com.dishcovery.app.domain.model.FeedAiResult
import com.dishcovery.app.domain.model.KitchenCategory
import com.dishcovery.app.domain.model.Recipe
import com.dishcovery.app.domain.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val apiService: RecipeApiService,
    private val mapper: RecipeMapper
) : RecipeRepository {


    private var cachedRecipes: List<Recipe> = emptyList()
    private var cachedCategories: List<KitchenCategory> = emptyList()


    override suspend fun getPopularRecipes(): Result<List<Recipe>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getPopularRecipes()

            if (!response.isSuccessful) {
                throw HttpException(response)
            }

            val recipes = mapper.toDomainList(response.body().orEmpty())

            cachedRecipes = recipes

            Result.success(recipes)
        } catch (e: Exception) {
            Result.failure(e.toUserFriendlyException())
        }
    }

    override suspend fun getKitchenCategories(): Result<List<KitchenCategory>> = withContext(Dispatchers.IO) {
        try {
            val categories = fetchCategoriesFromBackend()
            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e.toUserFriendlyException())
        }
    }

    override suspend fun getAiFeed(
        categoryId: Long,
        preference: String?,
        ingredients: List<String>?
    ): Result<FeedAiResult> = withContext(Dispatchers.IO) {
        try {
            val availableRecipes = fetchRecipesFromBackend()

            val response = apiService.getAiFeed(
                FeedAiRequestDto(
                    categoryId = categoryId,
                    preference = preference,
                    ingredients = ingredients
                )
            )

            if (!response.isSuccessful) {
                throw HttpException(response)
            }

            val body = response.body() ?: throw IllegalStateException("La respuesta del feed IA vino vacía")
            Result.success(mapper.toDomainFeed(body, availableRecipes))
        } catch (e: Exception) {
            Result.failure(e.toUserFriendlyException())
        }
    }

    override suspend fun getSavedRecipes(profileId: Long): Result<List<Recipe>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getSavedRecipes(profileId)

            if (!response.isSuccessful) {
                throw HttpException(response)
            }

            val recipes = mapper.toDomainList(response.body().orEmpty())
            Result.success(recipes)
        } catch (e: Exception) {
            Result.failure(e.toUserFriendlyException())
        }
    }

    override suspend fun saveRecipe(profileId: Long, recipeId: Long): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.saveRecipe(profileId, recipeId)

            if (!response.isSuccessful) {
                throw HttpException(response)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e.toUserFriendlyException())
        }
    }

    override suspend fun removeSavedRecipe(profileId: Long, recipeId: Long): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.removeSavedRecipe(profileId, recipeId)

            if (!response.isSuccessful) {
                throw HttpException(response)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e.toUserFriendlyException())
        }
    }

    private suspend fun fetchRecipesFromBackend(): List<Recipe> {
        val response = apiService.getRecipes()
        if (!response.isSuccessful) {
            throw HttpException(response)
        }

        val body = response.body().orEmpty()
        val recipes = mapper.toDomainList(body)
            .sortedByDescending { it.weight }
        cachedRecipes = recipes
        return recipes
    }

    private suspend fun fetchCategoriesFromBackend(): List<KitchenCategory> {
        val response = apiService.getKitchenCategories()
        if (!response.isSuccessful) {
            throw HttpException(response)
        }

        val body = response.body().orEmpty()
        val categories = mapper.toCategoryDomainList(body)
        cachedCategories = categories
        return categories
    }

    private fun Throwable.toUserFriendlyException(): Exception {
        val message = when (this) {
            is UnknownHostException,
            is SocketTimeoutException -> "Sin conexión o backend apagado. Verifica Mockoon y vuelve a intentar."
            is IOException -> "Error de red. Revisa tu conexión e intenta nuevamente."
            is HttpException -> "Error HTTP ${code()}: ${message}"
            else -> this.message ?: "Ocurrió un error inesperado."
        }
        return Exception(message, this)
    }
}