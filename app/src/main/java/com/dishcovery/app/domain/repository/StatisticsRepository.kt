package com.dishcovery.app.domain.repository

import com.dishcovery.app.domain.model.StatPoint

interface StatsRepository {
    suspend fun getActiveUsers(days: Int): Result<List<StatPoint>>
    suspend fun getMatches(days: Int): Result<List<StatPoint>>
}