package org.ptss.support.exceptions

import io.quarkus.logging.Log
import io.smallrye.faulttolerance.api.RateLimitException
import jakarta.inject.Inject
import jakarta.validation.ConstraintViolationException
import jakarta.ws.rs.ForbiddenException
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import org.eclipse.microprofile.faulttolerance.exceptions.BulkheadException
import org.eclipse.microprofile.faulttolerance.exceptions.CircuitBreakerOpenException
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException
import org.jboss.resteasy.reactive.ClientWebApplicationException
import org.ptss.support.core.context.IRequestContextService
import org.ptss.support.enums.ErrorCode

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
                errorCode = exception.errorCode,
                message = exception.message,
                requestId = requestId,
                details = exception.details
            )

            is ClientWebApplicationException -> {
                val message = exception.message ?: "Client error: ${exception.response.status}"
                Log.error("Client error occurred for request $requestId: $message")
                createResponse(
                    errorCode = ErrorCode.INTERNAL_ERROR,
                    message = message,
                    requestId = requestId
                )
            }

            is ForbiddenException -> {
                Log.warn("Unauthorized access attempt for request $requestId at path $path")
                createResponse(
                    errorCode = ErrorCode.INSUFFICIENT_PERMISSIONS,
                    message = "Client does not have the proper rights to access this resource",
                    requestId = requestId
                )
            }

            is ConstraintViolationException -> {
                val details = ErrorDetails(
                    constraints = exception.constraintViolations.associate { violation ->
                        violation.propertyPath.toString() to mapOf(
                            "constraint" to violation.constraintDescriptor.annotation.annotationClass.simpleName,
                            "message" to violation.message,
                            "value" to violation.invalidValue?.toString()
                        )
                    }
                )
                createResponse(
                    errorCode = ErrorCode.VALIDATION_ERROR,
                    message = "Validation failed",
                    requestId = requestId,
                    details = details
                )
            }

            is RateLimitException -> {
                Log.warn("Rate limit exceeded for request $requestId at path $path")
                createResponse(
                    errorCode = ErrorCode.RATE_LIMIT_EXCEEDED,
                    message = exception.message ?: "Too many concurrent requests",
                    requestId = requestId,
                )
            }

            is TimeoutException -> {
                Log.error("Request timeout for request $requestId at path $path")
                createResponse(
                    errorCode = ErrorCode.GATEWAY_TIMEOUT,
                    message = exception.message ?: "Request timed out",
                    requestId = requestId
                )
            }

            is CircuitBreakerOpenException -> {
                Log.error("Circuit breaker open for request $requestId at path $path")
                createResponse(
                    errorCode = ErrorCode.SERVICE_UNAVAILABLE,
                    message = exception.message ?: "Service temporarily unavailable",
                    requestId = requestId
                )
            }

            else -> {
                Log.error("Unhandled exception for request $requestId", exception)
                createResponse(
                    errorCode = ErrorCode.INTERNAL_ERROR,
                    message = "Internal server error",
                    requestId = requestId
                )
            }
        }
    }

    private fun createResponse(
        errorCode: ErrorCode,
        message: String,
        requestId: String? = null,
        details: ErrorDetails? = null
    ): Response {
        val error = ServiceError(
            code = errorCode.code,
            message = message,
            requestId = requestId,
            details = details
        )

        return Response.status(errorCode.status)
            .entity(error)
            .build()
    }
}