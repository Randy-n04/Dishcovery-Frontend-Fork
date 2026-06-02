package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.KitchenCategoryDto
import com.dishcovery.app.data.datasource.model.ProfileDto
import com.dishcovery.app.data.datasource.model.UserResultDto
import com.dishcovery.app.data.datasource.model.RoleDetailsDto

/**
 * Proveedor temporal de perfiles que utiliza los DTOs reales del backend.
 * Se ha eliminado la dependencia de AuthProvider.
 */
class ProfileProvider {

    companion object {

        private val avatars = listOf(
            "https://i.pravatar.cc/150?img=maria",
            "https://i.pravatar.cc/150?img=carlos",
            "https://i.pravatar.cc/150?img=andres",
            "https://i.pravatar.cc/150?img=luis",
            "https://i.pravatar.cc/150?img=sofia"
        )

        private val profiles: List<ProfileDto> = listOf(
            ProfileDto(
                id = 3,
                user = UserResultDto(3, "maria", RoleDetailsDto(2, "USER")),
                publicName = "Maria",
                bio = "Apasionada de la cocina mexicana",
                photoUrl = avatars[0],
                offeredSkills = listOf(KitchenCategoryDto(2L, "Comida Mexicana")),
                language = "Español",
                location = "CDMX"
            ),
            ProfileDto(
                id = 1,
                user = UserResultDto(1, "chefmario", RoleDetailsDto(1, "ADMIN")),
                publicName = "Chef Mario",
                bio = "Administrador y Chef Profesional",
                photoUrl = avatars[1],
                offeredSkills = emptyList(),
                language = "Español",
                location = "Costa Rica"
            )
        )

        fun getProfileByUserId(userId: Long): ProfileDto? =
            profiles.find { it.user.id == userId }
    }
}
