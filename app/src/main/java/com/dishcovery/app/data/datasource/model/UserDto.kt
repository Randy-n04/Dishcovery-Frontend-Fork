package com.dishcovery.app.data.datasource.model

import com.google.gson.annotations.SerializedName

/**
 * DTO para la solicitud de Login
 */
data class UserLoginInputDto(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

/**
 * DTO para la respuesta de Login (Spring Security UserDetails)
 */
data class LoginResponseDto(
    @SerializedName("username") val username: String,
    @SerializedName("authorities") val authorities: List<AuthorityDto>,
    @SerializedName("enabled") val enabled: Boolean,
    @SerializedName("accountNonExpired") val accountNonExpired: Boolean,
    @SerializedName("accountNonLocked") val accountNonLocked: Boolean,
    @SerializedName("credentialsNonExpired") val credentialsNonExpired: Boolean
)

data class AuthorityDto(
    @SerializedName("authority") val authority: String
)

/**
 * DTOs para operaciones de Usuario (CRUD)
 */
data class UserResultDto(
    @SerializedName("id") val id: Long,
    @SerializedName("username") val username: String,
    @SerializedName("role") val role: RoleDetailsDto
)

data class RoleDetailsDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String
)

data class UserInputDto(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("roleId") val roleId: Long? = null
)
