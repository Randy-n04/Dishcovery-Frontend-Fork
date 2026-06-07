package com.dishcovery.app.data.mapper

import com.dishcovery.app.data.datasource.model.KitchenCategoryDetailsDto
import com.dishcovery.app.data.datasource.model.KitchenCategoryDto
import com.dishcovery.app.data.datasource.model.KitchenCategoryResultDto
import com.dishcovery.app.domain.model.KitchenCategory
import javax.inject.Inject

class KitchenCategoryMapper @Inject constructor() {
    fun toDomain(dto: KitchenCategoryDto): KitchenCategory {
        return KitchenCategory(
            id = dto.id,
            name = dto.name
        )
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
}
