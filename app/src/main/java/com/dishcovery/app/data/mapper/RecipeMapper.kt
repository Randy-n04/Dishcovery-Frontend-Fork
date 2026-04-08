package com.dishcovery.app.data.mapper

import com.dishcovery.app.data.datasource.model.RecipeDto
import com.dishcovery.app.domain.model.Recipe
import javax.inject.Inject

class RecipeMapper @Inject constructor(
    private val categoryMapper: KitchenCategoryMapper
) {
    fun mapToDomain(dto: RecipeDto): Recipe {
        return Recipe(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            iconUrl = dto.iconUrl,
            weight = dto.weight,
            categories = dto.categories.map { categoryMapper.mapToDomain(it) }
        )
    }
}
