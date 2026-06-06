package com.dishcovery.app.domain.repository

import com.dishcovery.app.domain.model.ExchangePlan

interface MatchRepository {
    suspend fun generatePlan(
        profileId1: Long,
        profileId2: Long,
        categoryId: Long
    ): Result<ExchangePlan>
}