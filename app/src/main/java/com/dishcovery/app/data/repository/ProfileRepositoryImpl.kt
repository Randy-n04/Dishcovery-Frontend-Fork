package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.ProfileDataSource
import com.dishcovery.app.data.mapper.ProfileMapper
import com.dishcovery.app.domain.error.DomainError
import com.dishcovery.app.domain.repository.ProfileRepository
import com.dishcovery.app.domain.model.Profile
import javax.inject.Inject
import com.dishcovery.app.data.datasource.model.KitchenCategoryDetailsDto
import com.dishcovery.app.data.datasource.model.ProfileUpdateDto
import com.dishcovery.app.domain.model.KitchenCategory

class ProfileRepositoryImpl @Inject constructor(
    private val dataSource: ProfileDataSource,
    private val mapper: ProfileMapper
) : ProfileRepository {

    override suspend fun getProfileByUserId(userId: Long): Result<Profile> {
        return try {
            val dto = dataSource.getProfileByUserId(userId)
            if (dto != null) {
                // Sincronizado con el estándar toDomain
                Result.success(mapper.toDomain(dto))
            } else {
                Result.failure(DomainError.ProfileNotFound())
            }
        } catch (e: Exception) {
            Result.failure(DomainError.UnknownError(cause = e))
        }
    }

    override suspend fun updateProfile(
        profileId: Long,
        publicName: String?,
        bio: String?,
        photoUrl: String?,
        offeredSkills: List<KitchenCategory>?,
        skillsToLearn: List<KitchenCategory>?,
        language: String?,
        location: String?
    ): Result<Profile> {
        return try {
            val dto = ProfileUpdateDto(
                publicName = publicName,
                bio = bio,
                photoUrl = photoUrl,
                offeredSkills = offeredSkills?.map {
                    KitchenCategoryDetailsDto(
                        id = it.id,
                        name = it.name
                    )
                },
                skillsToLearn = skillsToLearn?.map {
                    KitchenCategoryDetailsDto(
                        id = it.id,
                        name = it.name
                    )
                },
                language = language,
                location = location
            )

            val updatedDto = dataSource.updateProfile(profileId, dto)

            if (updatedDto != null) {
                Result.success(mapper.toDomain(updatedDto))
            } else {
                Result.failure(DomainError.UnknownError())
            }
        } catch (e: Exception) {
            Result.failure(DomainError.UnknownError(cause = e))
        }
    }
}
