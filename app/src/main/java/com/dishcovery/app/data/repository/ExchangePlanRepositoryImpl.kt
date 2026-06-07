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

    override suspend fun getPlanByChatId(chatId: Long): Result<ExchangePlan> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.findByChatId(chatId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(mapper.toDomain(body))
                } else {
                    Result.failure(NoSuchElementException("No se encontró un plan para este chat."))
                }
            } else {
                Result.failure(Exception("Error al obtener el plan: ${response.code()}"))
            }
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
            val response = apiService.update(plan.id, mapper.toInputDto(plan))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(mapper.toDomain(body))
                } else {
                    Result.success(plan) // Si el servidor no devuelve cuerpo pero fue exitoso, devolvemos el mismo plan
                }
            } else {
                Result.failure(Exception("Error al actualizar el plan: ${response.code()}"))
            }
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
