package com.dishcovery.app.domain.repository

import com.dishcovery.app.domain.model.KitchenCategory
import com.dishcovery.app.domain.model.Profile

interface ProfileRepository {
    suspend fun getProfileByUserId(userId: Long): Result<Profile>

    suspend fun updateProfile(
        profileId: Long,
        publicName: String?,
        bio: String?,
        photoUrl: String?,
        offeredSkills: List<KitchenCategory>?,
        skillsToLearn: List<KitchenCategory>?,
        language: String?,
        location: String?
    ): Result<Profile>
}