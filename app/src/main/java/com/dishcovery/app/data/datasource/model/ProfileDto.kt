package com.dishcovery.app.data.datasource.model

/**
 * DTO para el Perfil en la capa de datos, vinculado al usuario real del backend.
 */
data class ProfileDto(
    val id: Long,
    val user: UserResultDto,
    val publicName: String,
    val bio: String,
    val photoUrl: String,
    val offeredSkills: List<KitchenCategoryDto>,
    val language: String,
    val location: String
)
