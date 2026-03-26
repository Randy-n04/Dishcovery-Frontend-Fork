package com.dishcovery.app.data.mapper

import com.dishcovery.app.data.datasource.model.KitchenCategoryDto
import com.dishcovery.app.domain.model.KitchenCategory

/**
 * Mapper between KitchenCategoryDto and KitchenCategory
 */
class KitchenCategoryMapper {

    fun mapToDomain(dto: KitchenCategoryDto): KitchenCategory = KitchenCategory(
        id   = dto.id,
        name = dto.name
    )
}
