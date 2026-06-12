package com.dishcovery.app.domain.repository

import com.dishcovery.app.domain.model.KitchenCategory

interface KitchenCategoryRepository {
    suspend fun getAll(): Result<List<KitchenCategory>>
    suspend fun getTrending(): Result<List<KitchenCategory>>
    suspend fun create(name: String): Result<KitchenCategory>
    suspend fun setTrending(id: Long, days: Int): Result<KitchenCategory>
}