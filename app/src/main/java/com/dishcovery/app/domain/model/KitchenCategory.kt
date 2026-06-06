package com.dishcovery.app.domain.model

data class KitchenCategory(
    val id: Long,
    val name: String,
    val trendingUntil: String? = null  // ISO string, se parsea en UI si hace falta
)
