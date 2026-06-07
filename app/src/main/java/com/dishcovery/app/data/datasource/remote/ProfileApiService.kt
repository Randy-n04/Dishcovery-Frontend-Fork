package com.dishcovery.app.data.datasource.remote

import com.dishcovery.app.data.datasource.model.ProfileDto
import retrofit2.http.GET
import retrofit2.http.Path
import com.dishcovery.app.data.datasource.model.ProfileUpdateDto
import retrofit2.http.Body
import retrofit2.http.PUT

interface ProfileApiService {

    @GET("v1/profiles/user/{userId}/no-recipes")
    suspend fun getProfileByUserId(@Path("userId") userId: Long): ProfileDto

    @PUT("v1/profiles/{id}")
    suspend fun updateProfile(
        @Path("id") id: Long,
        @Body profile: ProfileUpdateDto
    ): ProfileDto

}
