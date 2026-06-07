package com.dishcovery.app.domain.model

/**
 * AI recommendation metadata for a recipe.
 */
data class FeedAiRecipeSuggestion(
    val recipeId: Long,
    val rank: Int,
    val reason: String
)

