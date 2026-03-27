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
            "https://m.media-amazon.com/images/M/MV5BMTk5OTM3MzM2NF5BMl5BanBnXkFtZTgwOTM2MTY2MDI@._V1_FMjpg_UX1000_.jpg", //Juan
            "https://i.pravatar.cc/150?img=maria",
            "https://i.pravatar.cc/150?img=carlos",
            "https://i.pravatar.cc/150?img=andres",
            "https://i.pravatar.cc/150?img=luis",
            "https://i.pravatar.cc/150?img=sofia",
            "https://i.pravatar.cc/150?img=3",
            "https://i.pravatar.cc/150?img=valeria",
            "https://i.pravatar.cc/150?img=david",
            "https://i.pravatar.cc/150?img=laura",
            "https://i.pravatar.cc/150?img=1",
            "https://i.pravatar.cc/150?img=camila",
        )

        private val skills = listOf(
            listOf(KitchenCategoryDto(1L, "Comida Italiana")),
            listOf(KitchenCategoryDto(2L, "Comida Mexicana")),
            listOf(KitchenCategoryDto(3L, "Repostería")),
            listOf(KitchenCategoryDto(4L, "Comida Asiática")),
            listOf(KitchenCategoryDto(5L, "Parrilla"))
        )


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