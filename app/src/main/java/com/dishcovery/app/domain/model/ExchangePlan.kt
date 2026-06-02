package com.dishcovery.app.domain.model

/**
 * Representa un plan de intercambio completo entre dos usuarios.
 */
data class ExchangePlan(
    val id: Long = 0,
    val partnerId: Long,
    val partnerName: String,
    val partnerImageUrl: String,
    val sessions: List<ExchangeSession> = emptyList(),
    val aiSuggestion: String? = null,
    val progress: Int = 0,
    val totalSessions: Int = 0,
    val status: PlanStatus = PlanStatus.PENDING,
    val userNote: String = ""
)

enum class PlanStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    COMPLETED
}
