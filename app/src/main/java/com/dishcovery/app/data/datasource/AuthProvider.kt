package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.UserDto

/**
 * Simulates a data source (like a backend)
 */
class AuthProvider {

    companion object {

        private val users = listOf(
            UserDto(1, "admin", "1234"),
            UserDto(2, "user", "abcd")
        )

        fun login(username: String, password: String): UserDto? {
            return users.find {
                it.username == username && it.password == password
            }
        }
    }
}