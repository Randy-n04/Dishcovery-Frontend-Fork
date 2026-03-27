package com.dishcovery.app.domain.repository

import com.dishcovery.app.domain.model.Profile

/**
 * Contract for profile data operations.
 */
interface ProfileRepository {
    suspend fun getProfileByUserId(userId: Long): Result<Profile>
}