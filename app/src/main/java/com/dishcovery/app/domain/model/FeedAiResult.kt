package com.dishcovery.app.domain.model

/**
 * Domain model for the AI feed response.
 */
data class FeedAiResult(
    val aiTip: String,
    val suggestions: List<FeedAiRecipeSuggestion>,
    val recommendedRecipes: List<Recipe>
)

