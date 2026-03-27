package com.dishcovery.app.data.datasource.model

/**
 * DTO for Profile in data layer
 */
data class ProfileDto(
    val id: Long,
    val user: UserDto,
    val publicName: String,
    val bio: String,
    val photoUrl: String,
    val offeredSkills: List<KitchenCategoryDto>,
    val language: String,
    val location: String
)