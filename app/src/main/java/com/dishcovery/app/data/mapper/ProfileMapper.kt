package com.dishcovery.app.data.mapper

import com.dishcovery.app.data.datasource.model.ProfileDto
import com.dishcovery.app.domain.model.Profile
import com.dishcovery.app.domain.model.User
import com.dishcovery.app.domain.model.UserRole
import javax.inject.Inject

class ProfileMapper @Inject constructor(
    private val categoryMapper: KitchenCategoryMapper
) {
    fun toDomain(dto: ProfileDto): Profile = Profile(
        user = User(
            id = dto.userId,
            username = dto.username,
            role = UserRole.Client(2, "USER") // Default role as it's not in the profile response
        ),
        publicName   = dto.publicName,
        bio          = bioConverter(dto.bio),
        photoUrl     = dto.photoUrl,
        offeredSkills = dto.offeredSkills.map { categoryMapper.toDomain(it) },
        language     = dto.language,
        location     = dto.location
    )

    private fun bioConverter(bio: String): String {
        return bio.ifEmpty { "No bio available" }
    }
}
