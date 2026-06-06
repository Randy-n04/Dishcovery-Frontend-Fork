package com.dishcovery.app.domain.repository

import com.dishcovery.app.domain.model.ExchangePlan
import com.dishcovery.app.domain.model.Match

interface MatchRepository {
    suspend fun findMatches(
        profileId: Long,
        categoryIds: List<Long>
    ): Result<List<Match>>

    suspend fun generatePlan(
        profileId1: Long,
        profileId2: Long,
        categoryId: Long
    ): Result<ExchangePlan>
}
