package com.dishcovery.app.data.mapper

import com.dishcovery.app.data.datasource.model.UserDto
import com.dishcovery.app.domain.model.User

/**
 * Mapper between UserDto and User
 */
class UserMapper {

    fun mapToDomain(dto: UserDto): User = User(
        id = dto.id,
        username = dto.username,
        password = dto.password
    )

    fun mapToDto(domain: User): UserDto = UserDto(
        id = domain.id,
        username = domain.username,
        password = domain.password
    )
}