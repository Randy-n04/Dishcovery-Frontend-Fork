package com.dishcovery.app.domain.error

/**
 * Domain errors for authentication
 */
sealed class DomainError(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause) {

    class InvalidCredentials(
        message: String = "Invalid username or password"
    ) : DomainError(message)

    class UnknownError(
        message: String = "Unknown error occurred",
        cause: Throwable? = null
    ) : DomainError(message, cause)

    class ProfileNotFound(
        message: String = "Profile not found"
    ) : DomainError(message)
}