package com.dishcovery.app.domain.model


data class Profile(
    val id: Long,
    val user: User,
    val publicName: String,
    val bio: String,
    val photoUrl: String,
    val offeredSkills: List<KitchenCategory>,
    val skillsToLearn: List<KitchenCategory>,
    val language: String,
    val location: String
)