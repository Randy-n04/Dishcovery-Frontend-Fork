package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.StatPointDto
import com.dishcovery.app.data.datasource.remote.StatsApiService
import javax.inject.Inject

class StatsDataSourceImpl @Inject constructor(
    private val apiService: StatsApiService
) : StatsDataSource {

    override suspend fun getActiveUsers(days: Int): List<StatPointDto>? {
        return try { apiService.getActiveUsers(days) } catch (e: Exception) { null }
    }

    override suspend fun getMatches(days: Int): List<StatPointDto>? {
        return try { apiService.getMatches(days) } catch (e: Exception) { null }
    }
}