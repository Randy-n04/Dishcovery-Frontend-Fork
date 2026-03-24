package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.UserDto

/**
 * Simulates a data source (like a backend)
 */
class AuthProvider {

    companion object {

        private val users = listOf(
            UserDto(id = 1, username = "admin", password = "1234", role = "admin"),
            UserDto(id = 2, username = "user",  password = "abcd", role = "user")
        )

        fun login(username: String, password: String): UserDto? {
            return users.find { it.username == username && it.password == password }
        }
    }
}