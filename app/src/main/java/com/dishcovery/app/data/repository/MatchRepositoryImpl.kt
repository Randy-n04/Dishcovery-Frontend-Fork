package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.model.GeneratePlanRequestDto
import com.dishcovery.app.data.datasource.remote.MatchApiService
import com.dishcovery.app.data.mapper.ExchangePlanMapper
import com.dishcovery.app.domain.model.ExchangePlan
import com.dishcovery.app.domain.repository.MatchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MatchRepositoryImpl @Inject constructor(
    private val apiService: MatchApiService,
    private val mapper: ExchangePlanMapper
) : MatchRepository {

    override suspend fun generatePlan(
        profileId1: Long,
        profileId2: Long,
        categoryId: Long
    ): Result<ExchangePlan> = withContext(Dispatchers.IO) {
        try {
            val request = GeneratePlanRequestDto(
                profileId1 = profileId1,
                profileId2 = profileId2,
                categoryId = categoryId
            )

            val response = apiService.generatePlan(request)
            Result.success(mapper.toDomain(response))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}