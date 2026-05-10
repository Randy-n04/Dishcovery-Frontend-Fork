package com.dishcovery.app.data.mapper

import com.dishcovery.app.data.datasource.model.ProfileDto
import com.dishcovery.app.domain.model.Profile
import javax.inject.Inject

class ProfileMapper @Inject constructor(
    private val userMapper: UserMapper,
    private val categoryMapper: KitchenCategoryMapper
) {
    fun mapToDomain(dto: ProfileDto): Profile = Profile(
        user         = userMapper.mapToDomain(dto.user),
        publicName   = dto.publicName,
        bio          = bioConverter(dto.bio),
        photoUrl     = dto.photoUrl,
        offeredSkills = dto.offeredSkills.map { categoryMapper.mapToDomain(it) },
        language     = dto.language,
        location     = dto.location
    )

    private fun bioConverter(bio: String): String {
        return bio.ifEmpty { "No bio available" }
    }
}
