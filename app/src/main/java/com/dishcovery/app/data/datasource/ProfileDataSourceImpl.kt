package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.ProfileDto

class ProfileDataSourceImpl : ProfileDataSource {
    override suspend fun getProfileByUserId(userId: Long): ProfileDto? {
        return ProfileProvider.getProfileByUserId(userId)
    }
}