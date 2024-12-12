package org.ptss.support.common.exceptions

import org.eclipse.microprofile.openapi.annotations.media.Schema

@Schema(description = "Error response structure")
data class ServiceError(
    @field:Schema(description = "Error code identifier", example = "VALIDATION_ERROR", required = true)
    val code: String,

    @field:Schema(description = "Human readable error message", example = "Invalid email format provided", required = true)
    val message: String,

    @field:Schema(description = "Detailed error information")
    val details: ErrorDetails? = null,

    @field:Schema(description = "Unique identifier for the request", example = "550e8400-e29b-41d4-a716-446655440000")
    val requestId: String? = null,
)