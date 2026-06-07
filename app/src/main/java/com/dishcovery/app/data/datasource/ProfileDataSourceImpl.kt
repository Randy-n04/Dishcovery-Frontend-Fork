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
            apiService.getProfileByUserId(userId)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateProfile(
        profileId: Long,
        profileUpdateDto: ProfileUpdateDto
    ): ProfileDto? {
        return try {
            apiService.updateProfile(profileId, profileUpdateDto)
        } catch (e: Exception) {
            null
        }
    }
}