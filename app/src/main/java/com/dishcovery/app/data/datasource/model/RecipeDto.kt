package com.dishcovery.app.data.datasource.model

/**
 * Data Transfer Object for Recipe
 */
data class RecipeDto(
    val id: Long,
    val name: String,
    val description: String,
    val iconUrl: String,
    val weight: Int,
    val categories: List<KitchenCategoryDto>
)
