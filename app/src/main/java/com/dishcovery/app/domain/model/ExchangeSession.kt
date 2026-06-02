package com.dishcovery.app.domain.model

/**
 * Representa una sesión individual de cocina dentro de un plan de intercambio.
 */
data class ExchangeSession(
    val id: Long = 0,
    val title: String,
    val instructorName: String, // "Javier" o "Ti"
    val date: String,
    val time: String,
    val isCompleted: Boolean = false
)
