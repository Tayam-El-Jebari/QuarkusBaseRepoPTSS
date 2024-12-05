package org.ptss.support.exceptions

import io.quarkus.logging.Log
import jakarta.inject.Inject
import jakarta.validation.ConstraintViolationException
import jakarta.ws.rs.ForbiddenException
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import org.jboss.resteasy.reactive.ClientWebApplicationException
import org.ptss.support.core.context.IRequestContextService

@Provider
@Produces(MediaType.APPLICATION_JSON)
class GlobalExceptionHandler @Inject constructor(
    private val requestContextService: IRequestContextService
) : ExceptionMapper<Throwable> {

    override fun toResponse(exception: Throwable): Response {
        val requestId = requestContextService.getCurrentRequestId()
        val path = requestContextService.getCurrentPath()

        return when (exception) {
            is APIException -> createResponse(
                status = Response.Status.fromStatusCode(exception.errorCode.status),
                message = exception.message,
                errorCode = exception.errorCode.code,
                requestId = requestId,
                path = path,
            )

            is ClientWebApplicationException -> {
                val message = exception.message ?: "Client error: ${exception.response.status}"
                Log.error("Client error occurred for request $requestId: $message")
                createResponse(
                    status = Response.Status.fromStatusCode(exception.response.status),
                    message = message,
                    requestId = requestId,
                    path = path
                )
            }

            is ForbiddenException -> {
                Log.warn("Unauthorized access attempt for request $requestId at path $path")
                createResponse(
                    status = Response.Status.FORBIDDEN,
                    message = "Client does not have the proper rights to access this resource",
                    requestId = requestId,
                    path = path
                )
            }

            is ConstraintViolationException -> {
                val details = exception.constraintViolations.associate {
                    it.propertyPath.toString() to it.message
                }
                createResponse(
                    status = Response.Status.BAD_REQUEST,
                    message = "Validation failed",
                    requestId = requestId,
                    path = path,
                    details = details
                )
            }

            else -> {
                Log.error("Unhandled exception for request $requestId", exception)
                createResponse(
                    status = Response.Status.INTERNAL_SERVER_ERROR,
                    message = "Internal server error",
                    errorCode = "SYSTEM0001",
                    requestId = requestId,
                    path = path
                )
            }
        }
    }

    private fun createResponse(
        message: String,
        status: Response.Status,
        errorCode: String? = null,
        requestId: String? = null,
        path: String? = null,
        details: Map<String, Any>? = null
    ): Response {
        val error = ServiceError(
            status = status.statusCode,
            message = message,
            errorCode = errorCode ?: status.statusCode.toString(),
            requestId = requestId,
            path = path,
            details = details
        )

        return Response.status(status)
            .entity(error)
            .build()
    }
}