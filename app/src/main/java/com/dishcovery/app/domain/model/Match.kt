package com.dishcovery.app.domain.model

data class Match(
    val profileId: Long,
    val publicName: String,
    val photoUrl: String?,
    val location: String?,
    val offeredSkills: List<String>,
    val skillsToLearn: List<String>,
    val rank: Int,
    val reason: String?
)
