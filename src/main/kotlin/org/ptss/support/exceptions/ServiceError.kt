package org.ptss.support.exceptions

import org.eclipse.microprofile.openapi.annotations.media.Schema
import java.time.Instant

@Schema(name = "Error Response", description = "Standard error response object")
data class ServiceError(
    @Schema(description = "Timestamp when the error occurred")
    val timestamp: String = Instant.now().toString(),

    @Schema(description = "HTTP status code", example = "400")
    val status: Int,

    @Schema(description = "Application-specific error code", example = "AUTH001")
    val errorCode: String,

    @Schema(description = "Human-readable error message")
    val message: String,

    @Schema(description = "Request path where error occurred", example = "/api/resources/123")
    val path: String? = null,

    @Schema(description = "Unique identifier for the request")
    val requestId: String? = null,

    @Schema(description = "Additional error details")
    val details: Map<String, Any>? = null
)