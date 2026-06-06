package com.dishcovery.app.data.datasource.model

data class ExchangePlanResultDto(
    val id: Long,
    val partnerId: Long,
    val partnerName: String,
    val partnerImageUrl: String,
    val sessions: List<ExchangeSessionResultDto>,
    val aiSuggestion: String?,
    val progress: Int,
    val totalSessions: Int,
    val status: String,
    val userNote: String
)

data class ExchangePlanInputDto(
    val partnerId: Long?,
    val partnerName: String?,
    val partnerImageUrl: String?,
    val sessions: List<ExchangeSessionInputDto>?,
    val aiSuggestion: String?,
    val progress: Int?,
    val totalSessions: Int?,
    val status: String?,
    val userNote: String?
)

data class ExchangeSessionResultDto(
    val id: Long,
    val title: String,
    val instructorName: String,
    val date: String,
    val time: String,
    val isCompleted: Boolean
)

data class ExchangeSessionInputDto(
    val title: String?,
    val instructorName: String?,
    val date: String?,
    val time: String?,
    val isCompleted: Boolean?
)