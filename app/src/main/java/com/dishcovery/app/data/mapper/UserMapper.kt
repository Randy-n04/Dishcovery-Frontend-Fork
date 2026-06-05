package com.dishcovery.app.data.mapper

import com.dishcovery.app.data.datasource.model.UserResultDto
import com.dishcovery.app.data.datasource.model.LoginResponseDto
import com.dishcovery.app.domain.model.User
import com.dishcovery.app.domain.model.UserRole
import javax.inject.Inject

class UserMapper @Inject constructor() {

    /**
     * Mapea un usuario desde un resultado estándar de CRUD
     */
    fun toDomain(dto: UserResultDto): User {
        return User(
            id = dto.id,
            username = dto.username,
            role = mapRole(dto.role.id, dto.role.name)
        )
    }

    /**
     * Mapea el usuario desde la respuesta de Login de Spring Security
     */
    fun toDomainFromLogin(dto: LoginResponseDto): User {
        val isAdmin = dto.authorities.any { it.authority.uppercase() == "ADMIN" }
        return User(
            id = dto.id,
            username = dto.username,
            role = if (isAdmin) UserRole.Admin(1, "ADMIN") else UserRole.Client(2, "USER")
        )
    }

    fun toDomainList(dtos: List<UserResultDto>): List<User> {
        return dtos.map { toDomain(it) }
    }

    private fun mapRole(id: Long, name: String): UserRole {
        return when (name.uppercase()) {
            "ADMIN" -> UserRole.Admin(id, name)
            else -> UserRole.Client(id, name)
        }
    }
}
