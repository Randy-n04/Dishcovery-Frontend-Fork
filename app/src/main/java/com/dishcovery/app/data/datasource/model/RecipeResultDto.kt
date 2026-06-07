package com.dishcovery.app.data.datasource.model

/**
 * Data Transfer Object for recipes returned by the backend.
 */
data class RecipeResultDto(
    val id: Long = 0L,
    val name: String = "",
    val description: String = "",
    val iconUrl: String = "",
    val weight: Int = 0,
    val categories: List<KitchenCategoryDetailsDto> = emptyList(),
    val savedCount: Long = 0
)

