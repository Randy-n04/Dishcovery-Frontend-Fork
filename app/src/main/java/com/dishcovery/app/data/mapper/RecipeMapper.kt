package com.dishcovery.app.data.mapper

import com.dishcovery.app.data.datasource.model.FeedAiResponseDto
import com.dishcovery.app.data.datasource.model.KitchenCategoryDetailsDto
import com.dishcovery.app.data.datasource.model.KitchenCategoryResultDto
import com.dishcovery.app.data.datasource.model.RecipeResultDto
import com.dishcovery.app.domain.model.FeedAiRecipeSuggestion
import com.dishcovery.app.domain.model.FeedAiResult
import com.dishcovery.app.domain.model.KitchenCategory
import com.dishcovery.app.domain.model.Recipe
import javax.inject.Inject

class RecipeMapper @Inject constructor() {

    fun toDomain(dto: RecipeResultDto): Recipe {
        return Recipe(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            iconUrl = dto.iconUrl,
            weight = dto.weight,
            savedCount = dto.savedCount,
            categories = dto.categories.mapNotNull { toDomain(it) }
        )
    }

    fun toDomainList(dtos: List<RecipeResultDto>): List<Recipe> {
        return dtos.map { toDomain(it) }
    }

    fun toDomain(dto: KitchenCategoryResultDto): KitchenCategory {
        return KitchenCategory(
            id = dto.id,
            name = dto.name
        )
    }

    fun toDomain(dto: KitchenCategoryDetailsDto): KitchenCategory? {
        val id = dto.id ?: return null
        val name = dto.name?.takeIf { it.isNotBlank() } ?: return null

        return KitchenCategory(
            id = id,
            name = name
        )
    }

    fun toCategoryDomainList(categories: List<KitchenCategoryResultDto>): List<KitchenCategory> {
        return categories.map { toDomain(it) }
    }

    fun toDomainFeed(
        response: FeedAiResponseDto,
        recipes: List<Recipe>
    ): FeedAiResult {
        val orderedSuggestions = response.suggestions
            .sortedBy { it.rank }
            .map {
                FeedAiRecipeSuggestion(
                    recipeId = it.recipeId,
                    rank = it.rank,
                    reason = it.reason
                )
            }

        val recipesById = recipes.associateBy { it.id }
        val recommendedRecipes = orderedSuggestions.mapNotNull { suggestion ->
            recipesById[suggestion.recipeId]
        }

        return FeedAiResult(
            aiTip = response.aiTip,
            suggestions = orderedSuggestions,
            recommendedRecipes = recommendedRecipes
        )
    }
}
