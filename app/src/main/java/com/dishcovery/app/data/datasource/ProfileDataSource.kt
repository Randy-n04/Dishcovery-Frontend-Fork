package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.ProfileDto

/**
 * Contract for raw profile data retrieval.
 */
interface ProfileDataSource {
    suspend fun getProfileByUserId(userId: Long): ProfileDto?
}