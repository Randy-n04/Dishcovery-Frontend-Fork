package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.ProfileDto
import com.dishcovery.app.data.datasource.model.ProfileUpdateDto
import com.dishcovery.app.data.datasource.remote.ProfileApiService
import javax.inject.Inject

class ProfileDataSourceImpl @Inject constructor(
    private val apiService: ProfileApiService
) : ProfileDataSource {

    override suspend fun getProfileByUserId(userId: Long): ProfileDto? {
        return try {
            val response = apiService.getProfileByUserId(userId)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateProfile(
        profileId: Long,
        profileUpdateDto: ProfileUpdateDto
    ): ProfileDto? {
        return try {
            val response = apiService.updateProfile(profileId, profileUpdateDto)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
}