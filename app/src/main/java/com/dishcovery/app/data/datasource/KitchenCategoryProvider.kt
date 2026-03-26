package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.KitchenCategoryDto

/**
 * Simulates a data source for Kitchen Categories
 */
class KitchenCategoryProvider {

    companion object {
        val categories = listOf(
            KitchenCategoryDto(id = 1, name = "Italiana"),
            KitchenCategoryDto(id = 2, name = "Mexicana"),
            KitchenCategoryDto(id = 3, name = "Asiática"),
            KitchenCategoryDto(id = 4, name = "Postres"),
            KitchenCategoryDto(id = 5, name = "Saludable")
        )

        fun getAll(): List<KitchenCategoryDto> = categories

        fun getById(id: Long): KitchenCategoryDto? = categories.find { it.id == id }
    }
}
