package com.dishcovery.app.data.datasource.remote

import com.dishcovery.app.data.datasource.model.StatPointDto
import retrofit2.http.GET
import retrofit2.http.Query

interface StatsApiService {
    @GET("v1/stats/active-users")
    suspend fun getActiveUsers(@Query("days") days: Int = 30): List<StatPointDto>

    @GET("v1/stats/matches")
    suspend fun getMatches(@Query("days") days: Int = 30): List<StatPointDto>
}