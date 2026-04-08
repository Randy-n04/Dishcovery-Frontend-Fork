package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.ProfileDataSource
import com.dishcovery.app.data.mapper.ProfileMapper
import com.dishcovery.app.domain.error.DomainError
import com.dishcovery.app.domain.repository.ProfileRepository
import com.dishcovery.app.domain.model.Profile
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val dataSource: ProfileDataSource,
    private val mapper: ProfileMapper
) : ProfileRepository {

    override suspend fun getProfileByUserId(userId: Long): Result<Profile> {
        return try {
            val dto = dataSource.getProfileByUserId(userId)
            if (dto != null) {
                Result.success(mapper.mapToDomain(dto))
            } else {
                Result.failure(DomainError.ProfileNotFound())
            }
        } catch (e: Exception) {
            Result.failure(DomainError.UnknownError(cause = e))
        }
    }
}
