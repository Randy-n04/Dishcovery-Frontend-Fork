package com.dishcovery.app.data.mapper

import com.dishcovery.app.data.datasource.model.UserDto
import com.dishcovery.app.domain.model.User
import com.dishcovery.app.domain.model.UserRole

/**
 * Mapper between UserDto and User
 */
class UserMapper {

    fun mapToDomain(dto: UserDto): User = User(
        id       = dto.id,
        username = dto.username,
        password = dto.password,
        role     = when (dto.role.lowercase()) {
            "admin" -> UserRole.Admin
            else    -> UserRole.User
        }
    )

    fun mapToDto(domain: User): UserDto = UserDto(
        id       = domain.id,
        username = domain.username,
        password = domain.password,
        role     = when (domain.role) {
            is UserRole.Admin -> "admin"
            is UserRole.User  -> "user"
        }
    )
}