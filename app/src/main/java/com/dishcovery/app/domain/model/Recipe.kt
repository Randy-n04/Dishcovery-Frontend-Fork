package com.dishcovery.app.domain.model

data class Recipe(
    val id: Long,
    val name: String,
    val description: String,
    val iconUrl: String,
    val weight: Int,
    val categories: List<KitchenCategory> = emptyList()
)
