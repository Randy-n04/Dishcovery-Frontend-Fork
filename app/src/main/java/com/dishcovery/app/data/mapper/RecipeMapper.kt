package com.dishcovery.app.data.mapper

import com.dishcovery.app.data.datasource.model.RecipeDto
import com.dishcovery.app.domain.model.Recipe

/**
 * Mapper between RecipeDto and Recipe
 */
class RecipeMapper(
    private val categoryMapper: KitchenCategoryMapper = KitchenCategoryMapper()
) {

    fun mapToDomain(dto: RecipeDto): Recipe = Recipe(
        id          = dto.id,
        name        = dto.name,
        description = dto.description,
        iconUrl     = dto.iconUrl,
        weight      = dto.weight,
        categories  = dto.categories.map { categoryMapper.mapToDomain(it) }
    )
}
