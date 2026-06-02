package com.dishcovery.app.data.mapper

import com.dishcovery.app.data.datasource.model.KitchenCategoryDto
import com.dishcovery.app.domain.model.KitchenCategory
import javax.inject.Inject

class KitchenCategoryMapper @Inject constructor() {
    fun toDomain(dto: KitchenCategoryDto): KitchenCategory {
        return KitchenCategory(
            id = dto.id,
            name = dto.name
        )
    }
}
