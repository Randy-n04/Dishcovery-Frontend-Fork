package com.dishcovery.app.data.datasource.model

data class FaqDto(
    val id: Long,
    val content: String
)

data class FaqUpdateRequest(
    val content: String
)