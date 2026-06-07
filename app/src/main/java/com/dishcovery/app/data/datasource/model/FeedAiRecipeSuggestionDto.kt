package com.dishcovery.app.data.datasource.model

/**
 * Suggestion returned by the AI feed endpoint.
 */
data class FeedAiRecipeSuggestionDto(
    val recipeId: Long = 0L,
    val rank: Int = 0,
    val reason: String = ""
)

