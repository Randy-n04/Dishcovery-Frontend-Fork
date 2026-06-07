package com.dishcovery.app.domain.error

import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Domain errors for the application
 */
sealed class DomainError(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause) {

    class InvalidCredentials(
        message: String = "Usuario o contraseña incorrectos"
    ) : DomainError(message)

    class NetworkError(
        message: String = "Error de conexión. Revisa tu internet."
    ) : DomainError(message)

    class ServerError(
        message: String = "El servidor no responde. Inténtalo más tarde."
    ) : DomainError(message)

    class ProfileNotFound(
        message: String = "Perfil no encontrado"
    ) : DomainError(message)

    class UnknownError(
        message: String = "Ha ocurrido un error inesperado",
        cause: Throwable? = null
    ) : DomainError(message, cause)

    class DataError(
        message: String = "El servidor envió información incompleta o inválida."
    ) : DomainError(message)

    companion object {
        fun fromThrowable(t: Throwable): DomainError {
            if (t is DomainError) return t
            
            val msg = (t.message ?: t.toString()).lowercase()
            
            // Filtro agresivo para errores técnicos de GSON/Retrofit (como el de la imagen)
            if (msg.contains("end of input") || 
                msg.contains("path $") || 
                msg.contains("malformed") || 
                msg.contains("expected") || 
                msg.contains("eof") ||
                msg.contains("syntax error")) {
                return DataError()
            }

            return when (t) {
                is ConnectException, is UnknownHostException -> NetworkError()
                is SocketTimeoutException -> NetworkError("La conexión ha expirado. Inténtalo de nuevo.")
                is IOException -> NetworkError()
                is HttpException -> {
                    when (t.code()) {
                        401 -> InvalidCredentials()
                        404 -> ProfileNotFound()
                        in 500..599 -> ServerError()
                        else -> UnknownError(cause = t)
                    }
                }
                else -> UnknownError(cause = t)
            }
        }
    }
}
