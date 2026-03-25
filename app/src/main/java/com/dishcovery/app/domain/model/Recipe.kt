package com.dishcovery.app.domain.model

data class Recipe(
    val id: Int,
    val title: String,
    val description: String = ""
)
