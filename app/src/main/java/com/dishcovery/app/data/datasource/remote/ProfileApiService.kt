package com.dishcovery.app.data.datasource.remote

import com.dishcovery.app.data.datasource.model.ProfileDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileApiService {

    @GET("v1/profiles/user/{userId}/no-recipes")
    suspend fun getProfileByUserId(@Path("userId") userId: Long): ProfileDto
}
