package com.dishcovery.app.data.datasource.remote

import com.dishcovery.app.data.datasource.model.ExchangePlanResultDto
import com.dishcovery.app.data.datasource.model.GeneratePlanRequestDto
import com.dishcovery.app.data.datasource.model.MatchRequestDto
import com.dishcovery.app.data.datasource.model.MatchResultDto
import retrofit2.http.Body
import retrofit2.http.POST

interface MatchApiService {

    @POST("v1/match/find")
    suspend fun findMatches(
        @Body request: MatchRequestDto
    ): List<MatchResultDto>

    @POST("v1/match/generate-plan")
    suspend fun generatePlan(
        @Body request: GeneratePlanRequestDto
    ): ExchangePlanResultDto
}
