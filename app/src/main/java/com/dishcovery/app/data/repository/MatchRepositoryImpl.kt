package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.model.GeneratePlanRequestDto
import com.dishcovery.app.data.datasource.model.MatchRequestDto
import com.dishcovery.app.data.datasource.remote.MatchApiService
import com.dishcovery.app.data.mapper.ExchangePlanMapper
import com.dishcovery.app.data.mapper.MatchMapper
import com.dishcovery.app.domain.model.ExchangePlan
import com.dishcovery.app.domain.model.Match
import com.dishcovery.app.domain.repository.MatchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MatchRepositoryImpl @Inject constructor(
    private val apiService: MatchApiService,
    private val exchangePlanMapper: ExchangePlanMapper,
    private val matchMapper: MatchMapper
) : MatchRepository {

    override suspend fun findMatches(
        profileId: Long,
        categoryIds: List<Long>
    ): Result<List<Match>> = withContext(Dispatchers.IO) {
        try {
            val request = MatchRequestDto(profileId, categoryIds)
            val response = apiService.findMatches(request)
            Result.success(matchMapper.toDomainList(response))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(exchangePlanMapper.toDomain(body))
                } else {
                    Result.failure(Exception("El servidor generó un plan vacío."))
                }
            } else {
                Result.failure(Exception("Error al generar plan (${response.code()})"))
            }
        } catch (e: Exception) {
            if (e.message?.contains("End of input") == true) {
                Result.failure(Exception("No se pudo procesar la respuesta del servidor (Vacía)"))
            } else {
                Result.failure(e)
            }
        }
    }
}
