package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.KitchenCategoryDto
import com.dishcovery.app.data.datasource.model.KitchenCategoryResultDto

interface KitchenCategoryDataSource {
    suspend fun getAll(): List<KitchenCategoryDto>?
    suspend fun getTrending(): List<KitchenCategoryDto>?
    suspend fun create(name: String): KitchenCategoryResultDto?
    suspend fun setTrending(id: Long, days: Int): KitchenCategoryResultDto?
}