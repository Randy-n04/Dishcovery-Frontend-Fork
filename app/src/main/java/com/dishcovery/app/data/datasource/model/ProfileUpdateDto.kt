package com.dishcovery.app.data.datasource.model

data class ProfileUpdateDto(
    val publicName: String?,
    val bio: String?,
    val photoUrl: String?,
    val offeredSkills: List<KitchenCategoryDetailsDto>?,
    val skillsToLearn: List<KitchenCategoryDetailsDto>?,
    val language: String?,
    val location: String?
)