package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.StatPointDto

interface StatsDataSource {
    suspend fun getActiveUsers(days: Int): List<StatPointDto>?
    suspend fun getMatches(days: Int): List<StatPointDto>?
}