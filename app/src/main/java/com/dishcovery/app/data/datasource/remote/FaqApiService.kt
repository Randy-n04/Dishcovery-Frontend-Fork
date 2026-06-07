package com.dishcovery.app.data.datasource.remote

import com.dishcovery.app.data.datasource.model.FaqDto
import com.dishcovery.app.data.datasource.model.FaqUpdateRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface FaqApiService {
    @GET("v1/faq")
    suspend fun getFaq(): FaqDto

    @PUT("v1/faq")
    suspend fun updateFaq(@Body body: FaqUpdateRequest): FaqDto
}