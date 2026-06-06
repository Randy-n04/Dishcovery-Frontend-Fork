package com.dishcovery.app.data.datasource.model

import com.google.gson.annotations.SerializedName

data class GeneratePlanRequestDto(
    val profileId1: Long,
    val profileId2: Long,
    val categoryId: Long
)

data class MatchRequestDto(
    @SerializedName("profileId") val profileId: Long,
    @SerializedName("categoryIds") val categoryIds: List<Long>
)

data class MatchResultDto(
    @SerializedName("profileId") val profileId: Long,
    @SerializedName("publicName") val publicName: String,
    @SerializedName("photoUrl") val photoUrl: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("offeredSkills") val offeredSkills: List<String>,
    @SerializedName("skillsToLearn") val skillsToLearn: List<String>,
    @SerializedName("rank") val rank: Int,
    @SerializedName("reason") val reason: String?
)
