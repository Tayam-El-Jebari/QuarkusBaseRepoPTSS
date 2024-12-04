package org.ptss.support.exceptions

import io.quarkus.logging.Log
import jakarta.validation.ConstraintViolationException
import jakarta.ws.rs.ForbiddenException
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import org.jboss.resteasy.reactive.ClientWebApplicationException
@Provider
class GlobalExceptionHandler(
) : ExceptionMapper<Throwable> {

    override fun toResponse(exception: Throwable): Response {
        return when (exception) {
            is APIException -> createResponse(
                status = Response.Status.fromStatusCode(exception.errorCode.status),
                message = exception.message,
                errorCode = exception.errorCode.code
            )

            is ClientWebApplicationException -> {
                val message = exception.message ?:
                "Client error: ${exception.response.status}"
                Log.error(message)
                createResponse(
                    status = Response.Status.fromStatusCode(exception.response.status),
                    message = message
                )
            }

            is ForbiddenException -> {
                Log.warn("Client tried to access resource without proper permissions")
                createResponse(
                    status = Response.Status.FORBIDDEN,
                    message = "Client does not have the proper rights to access this resource"
                )
            }

            is ConstraintViolationException -> {
                createResponse(
                    status = Response.Status.BAD_REQUEST,
                    message = exception.constraintViolations.joinToString(separator = "\n") {
                        it.message
                    }
                )
            }

            else -> {
                Log.error("Unhandled exception", exception)
                createResponse(
                    status = Response.Status.INTERNAL_SERVER_ERROR,
                    message = "Internal server error",
                    errorCode = "SYSTEM0001"
                )
            }
        }
    }

    private fun createResponse(
        message: String,
        status: Response.Status,
        errorCode: String? = null
    ): Response =
        Response.status(status)
            .type(MediaType.APPLICATION_JSON)
            .entity(
                ServiceError(
                    status = status.statusCode,
                    message = message,
                    errorCode = errorCode ?: status.statusCode.toString()
                )
            )
            .build()
}