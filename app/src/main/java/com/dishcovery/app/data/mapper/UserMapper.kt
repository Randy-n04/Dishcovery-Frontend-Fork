package com.dishcovery.app.data.mapper

import com.dishcovery.app.data.datasource.model.UserDto
import com.dishcovery.app.domain.model.User
import com.dishcovery.app.domain.model.UserRole
import javax.inject.Inject

/**
 * Mapper between UserDto and User
 */
class UserMapper @Inject constructor() {

    fun mapToDomain(dto: UserDto): User = User(
        id       = dto.id,
        username = dto.username,
        password = dto.password,
        role     = when (dto.role.lowercase()) {
            "admin" -> UserRole.Admin
            else    -> UserRole.User
        }
    )
}
