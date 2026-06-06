package com.dishcovery.app.data.datasource.model

data class GeneratePlanRequestDto(
    val profileId1: Long,
    val profileId2: Long,
    val categoryId: Long
)