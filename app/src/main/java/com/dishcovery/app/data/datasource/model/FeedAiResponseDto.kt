package com.dishcovery.app.data.datasource.model

/**
 * Response payload for the AI feed endpoint.
 */
data class FeedAiResponseDto(
    val aiTip: String = "",
    val suggestions: List<FeedAiRecipeSuggestionDto> = emptyList()
)

