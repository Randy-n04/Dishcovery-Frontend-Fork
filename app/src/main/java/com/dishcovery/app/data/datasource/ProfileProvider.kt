package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.KitchenCategoryDto
import com.dishcovery.app.data.datasource.model.ProfileDto

/**
 * Temporary in-memory data provider for profile data.
 * Mirrors the users created in AuthProvider.
 */
class ProfileProvider {

    companion object {

        private val avatars = listOf(
            "https://randomuser.me/api/portraits/men/1.jpg",
            "https://randomuser.me/api/portraits/women/1.jpg",
            "https://randomuser.me/api/portraits/men/11.jpg",
            "https://randomuser.me/api/portraits/women/12.jpg",
            "https://randomuser.me/api/portraits/women/13.jpg",
            "https://randomuser.me/api/portraits/men/14.jpg",
            "https://randomuser.me/api/portraits/women/15.jpg",
            "https://randomuser.me/api/portraits/men/2.jpg",
            "https://randomuser.me/api/portraits/women/3.jpg",
            "https://randomuser.me/api/portraits/men/3.jpg",
            "https://randomuser.me/api/portraits/women/4.jpg",
            "https://randomuser.me/api/portraits/men/4.jpg",
            "https://randomuser.me/api/portraits/women/5.jpg"
        )

        private val skills = listOf(
            listOf(KitchenCategoryDto(1L, "Comida Italiana")),
            listOf(KitchenCategoryDto(2L, "Comida Mexicana")),
            listOf(KitchenCategoryDto(3L, "Repostería")),
            listOf(KitchenCategoryDto(4L, "Comida Asiática")),
            listOf(KitchenCategoryDto(5L, "Parrilla"))
        )

        // Genera un perfil por cada usuario de AuthProvider automáticamente
        private val profiles: List<ProfileDto> by lazy {
            AuthProvider.getUsers()
                .filter { it.role == "user" }
                .mapIndexed { index, userDto ->
                    ProfileDto(
                        id            = userDto.id,
                        user          = userDto,
                        publicName    = userDto.username.replaceFirstChar { it.uppercase() },
                        bio           = "Apasionado/a de la cocina",
                        photoUrl      = avatars[index % avatars.size],
                        offeredSkills = skills[index % skills.size],
                        language      = "Español",
                        location      = "San José, Costa Rica"
                    )
                }
        }

        fun getProfileByUserId(userId: Long): ProfileDto? =
            profiles.find { it.user.id == userId }
    }
}