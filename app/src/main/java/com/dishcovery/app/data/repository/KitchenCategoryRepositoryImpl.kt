package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.KitchenCategoryDataSource
import com.dishcovery.app.data.mapper.KitchenCategoryMapper
import com.dishcovery.app.domain.error.DomainError
import com.dishcovery.app.domain.model.KitchenCategory
import com.dishcovery.app.domain.repository.KitchenCategoryRepository
import jakarta.inject.Inject

class KitchenCategoryRepositoryImpl @Inject constructor(
    private val dataSource: KitchenCategoryDataSource,
    private val mapper: KitchenCategoryMapper
) : KitchenCategoryRepository {

    override suspend fun getAll(): Result<List<KitchenCategory>> {
        return try {
            val list = dataSource.getAll()
            if (list != null) Result.success(list.mapNotNull { mapper.toDomain(it) })
            else Result.failure(DomainError.UnknownError())
        } catch (e: Exception) {
            Result.failure(DomainError.UnknownError(cause = e))
        }
    }

    override suspend fun getTrending(): Result<List<KitchenCategory>> {
        return try {
            val list = dataSource.getTrending()
            if (list != null) Result.success(list.mapNotNull { mapper.toDomain(it) })
            else Result.failure(DomainError.UnknownError())
        } catch (e: Exception) {
            Result.failure(DomainError.UnknownError(cause = e))
        }
    }

    override suspend fun create(name: String): Result<KitchenCategory> {
        return try {
            val dto = dataSource.create(name)
            if (dto != null) Result.success(mapper.toDomain(dto))
            else Result.failure(DomainError.UnknownError())
        } catch (e: Exception) {
            Result.failure(DomainError.UnknownError(cause = e))
        }
    }

    override suspend fun setTrending(id: Long, days: Int): Result<KitchenCategory> {
        return try {
            val dto = dataSource.setTrending(id, days)
            if (dto != null) Result.success(mapper.toDomain(dto))
            else Result.failure(DomainError.UnknownError())
        } catch (e: Exception) {
            Result.failure(DomainError.UnknownError(cause = e))
        }
    }
}