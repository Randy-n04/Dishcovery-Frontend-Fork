package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.remote.ExchangePlanApiService
import com.dishcovery.app.data.mapper.ExchangePlanMapper
import com.dishcovery.app.domain.model.ExchangePlan
import com.dishcovery.app.domain.repository.ExchangePlanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExchangePlanRepositoryImpl @Inject constructor(
    private val apiService: ExchangePlanApiService,
    private val mapper: ExchangePlanMapper
) : ExchangePlanRepository {

    override suspend fun getAllPlans(): Result<List<ExchangePlan>> = withContext(Dispatchers.IO) {
        try {
            val plans = apiService.findAll().map { mapper.toDomain(it) }
            Result.success(plans)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPlanById(id: Long): Result<ExchangePlan> = withContext(Dispatchers.IO) {
        try {
            val plan = mapper.toDomain(apiService.findById(id))
            Result.success(plan)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createPlan(plan: ExchangePlan): Result<ExchangePlan> = withContext(Dispatchers.IO) {
        try {
            val created = apiService.create(mapper.toInputDto(plan))
            Result.success(mapper.toDomain(created))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePlan(plan: ExchangePlan): Result<ExchangePlan> = withContext(Dispatchers.IO) {
        try {
            val updated = apiService.update(plan.id, mapper.toInputDto(plan))
            Result.success(mapper.toDomain(updated))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deletePlan(id: Long): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            apiService.deleteById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}