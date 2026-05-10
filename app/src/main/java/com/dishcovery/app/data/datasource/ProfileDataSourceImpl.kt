package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.ProfileDto
import javax.inject.Inject

class ProfileDataSourceImpl @Inject constructor() : ProfileDataSource {
    override suspend fun getProfileByUserId(userId: Long): ProfileDto? {
        return ProfileProvider.getProfileByUserId(userId)
    }
}
