package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.StatsDataSource
import com.dishcovery.app.data.mapper.StatsMapper
import com.dishcovery.app.domain.error.DomainError
import com.dishcovery.app.domain.model.StatPoint
import com.dishcovery.app.domain.repository.StatsRepository
import javax.inject.Inject

class StatsRepositoryImpl @Inject constructor(
    private val dataSource: StatsDataSource,
    private val mapper: StatsMapper
) : StatsRepository {

    override suspend fun getActiveUsers(days: Int): Result<List<StatPoint>> {
        return try {
            val list = dataSource.getActiveUsers(days)
            if (list != null) Result.success(list.map { mapper.toDomain(it) })
            else Result.failure(DomainError.UnknownError())
        } catch (e: Exception) {
            Result.failure(DomainError.UnknownError(cause = e))
        }
    }

    override suspend fun getMatches(days: Int): Result<List<StatPoint>> {
        return try {
            val list = dataSource.getMatches(days)
            if (list != null) Result.success(list.map { mapper.toDomain(it) })
            else Result.failure(DomainError.UnknownError())
        } catch (e: Exception) {
            Result.failure(DomainError.UnknownError(cause = e))
        }
    }
}