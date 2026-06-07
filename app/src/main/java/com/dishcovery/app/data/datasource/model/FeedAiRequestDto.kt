package com.dishcovery.app.data.datasource.model

/**
 * Request payload for the AI feed endpoint.
 */
data class FeedAiRequestDto(
    val categoryId: Long = 0L,
    val preference: String? = null,
    val ingredients: List<String>? = null
)

