package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.remote.ExchangePlanApiService
import com.dishcovery.app.data.mapper.ExchangePlanMapper
import com.dishcovery.app.domain.error.DomainError
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
            val response = apiService.findAll()
            if (response.isSuccessful) {
                val body = response.body()
                val plans = body?.map { mapper.toDomain(it) } ?: emptyList()
                Result.success(plans)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Throwable) {
            Result.failure(DomainError.fromThrowable(e))
        }
    }

    override suspend fun getPlanById(id: Long): Result<ExchangePlan> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.findById(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(mapper.toDomain(body))
                } else {
                    Result.failure(NoSuchElementException("Cuerpo vacío"))
                }
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Throwable) {
            Result.failure(DomainError.fromThrowable(e))
        }
    }

    override suspend fun getPlanByChatId(chatId: Long): Result<ExchangePlan> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.findByChatId(chatId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(mapper.toDomain(body))
                } else {
                    Result.failure(NoSuchElementException("Sin plan"))
                }
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Throwable) {
            Result.failure(DomainError.fromThrowable(e))
        }
    }

    override suspend fun createPlan(plan: ExchangePlan): Result<ExchangePlan> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.create(mapper.toInputDto(plan))
            if (response.isSuccessful && response.body() != null) {
                Result.success(mapper.toDomain(response.body()!!))
            } else {
                Result.success(plan)
            }
        } catch (e: Throwable) {
            Result.success(plan)
        }
    }

    override suspend fun updatePlan(plan: ExchangePlan): Result<ExchangePlan> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.update(plan.id, mapper.toInputDto(plan))
            if (response.isSuccessful && response.body() != null) {
                Result.success(mapper.toDomain(response.body()!!))
            } else {
                Result.success(plan)
            }
        } catch (e: Throwable) {
            Result.success(plan)
        }
    }

    override suspend fun deletePlan(id: Long): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            apiService.deleteById(id)
            Result.success(Unit)
        } catch (e: Throwable) {
            Result.success(Unit)
        }
    }
}
