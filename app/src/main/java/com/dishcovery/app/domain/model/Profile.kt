package com.dishcovery.app.domain.model

/**
 * Represents a user's public profile in the system.
 */
data class Profile(
    val id: Long,
    val user: User,
    val publicName: String,
    val bio: String,
    val photoUrl: String,
    val offeredSkills: List<KitchenCategory>,
    val language: String,
    val location: String
)