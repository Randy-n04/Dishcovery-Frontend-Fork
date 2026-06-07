package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.ProfileDto
import com.dishcovery.app.data.datasource.model.ProfileUpdateDto

interface ProfileDataSource {
    suspend fun getProfileByUserId(userId: Long): ProfileDto?

    suspend fun updateProfile(
        profileId: Long,
        profileUpdateDto: ProfileUpdateDto
    ): ProfileDto?
}