package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.UserDto

/**
 * Simulates a data source (like a backend)
 */
class AuthProvider {

    companion object {

        private val users = listOf(
            UserDto(id = 1, username = "admin", password = "1234", role = "admin"),
            UserDto(id = 2, username = "juan", password = "1234", role = "user"),
            UserDto(id = 3, username = "maria", password = "1234", role = "user"),
            UserDto(id = 4, username = "carlos", password = "1234", role = "user"),
            UserDto(id = 5, username = "ana", password = "1234", role = "user"),
            UserDto(id = 6, username = "luis", password = "1234", role = "user"),
            UserDto(id = 7, username = "sofia", password = "1234", role = "user"),
            UserDto(id = 8, username = "andres", password = "1234", role = "user"),
            UserDto(id = 9, username = "valeria", password = "1234", role = "user"),
            UserDto(id = 10, username = "david", password = "1234", role = "user"),
            UserDto(id = 11, username = "laura", password = "1234", role = "user"),
            UserDto(id = 12, username = "jose", password = "1234", role = "user"),
            UserDto(id = 13, username = "camila", password = "1234", role = "user")
        )

        fun login(username: String, password: String): UserDto? {
            return users.find { it.username == username && it.password == password }
        }

        /** Exposes the full user list for other providers. */
        fun getUsers(): List<UserDto> = users
    }
}