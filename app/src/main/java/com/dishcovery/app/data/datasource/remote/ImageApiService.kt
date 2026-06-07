package com.dishcovery.app.data.datasource.remote

import com.dishcovery.app.data.datasource.model.ImageAnalysisDTO
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageApiService {

    @Multipart
    @POST("v1/food-analysis/analyze")
    suspend fun analyzeImage(
        @Part image: MultipartBody.Part
    ): Response<ImageAnalysisDTO>
}