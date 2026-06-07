package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.KitchenCategoryDetailsDto
import com.dishcovery.app.data.datasource.model.KitchenCategoryDto
import com.dishcovery.app.data.datasource.model.KitchenCategoryResultDto
import com.dishcovery.app.data.datasource.model.TrendingRequest
import com.dishcovery.app.data.datasource.remote.KitchenCategoryApiService
import jakarta.inject.Inject

class KitchenCategoryDataSourceImpl @Inject constructor(
    private val apiService: KitchenCategoryApiService
) : KitchenCategoryDataSource {

    override suspend fun getAll(): List<KitchenCategoryDto>? {
        return try { apiService.getAll() } catch (e: Exception) { null }
    }

    override suspend fun getTrending(): List<KitchenCategoryDto>? {
        return try { apiService.getTrending() } catch (e: Exception) { null }
    }

    override suspend fun create(name: String): KitchenCategoryResultDto? {
        return try {
            apiService.create(KitchenCategoryDetailsDto(name = name))
        } catch (e: Exception) { null }
    }

    override suspend fun setTrending(id: Long, days: Int): KitchenCategoryResultDto? {
        return try {
            apiService.setTrending(id, TrendingRequest(days))
        } catch (e: Exception) { null }
    }
}