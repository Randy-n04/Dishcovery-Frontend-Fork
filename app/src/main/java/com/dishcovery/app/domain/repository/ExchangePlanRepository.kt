package com.dishcovery.app.domain.repository

import com.dishcovery.app.domain.model.ExchangePlan

interface ExchangePlanRepository {
    suspend fun getAllPlans(): Result<List<ExchangePlan>>
    suspend fun getPlanById(id: Long): Result<ExchangePlan>
    suspend fun createPlan(plan: ExchangePlan): Result<ExchangePlan>
    suspend fun updatePlan(plan: ExchangePlan): Result<ExchangePlan>
    suspend fun deletePlan(id: Long): Result<Unit>
}