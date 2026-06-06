package com.dishcovery.app.data.datasource.remote

import com.dishcovery.app.data.datasource.model.ExchangePlanResultDto
import com.dishcovery.app.data.datasource.model.GeneratePlanRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface MatchApiService {

    @POST("v1/match/generate-plan")
    suspend fun generatePlan(
        @Body request: GeneratePlanRequestDto
    ): ExchangePlanResultDto
}