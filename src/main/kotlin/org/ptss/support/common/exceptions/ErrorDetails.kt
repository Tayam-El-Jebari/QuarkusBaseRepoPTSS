package org.ptss.support.common.exceptions

import org.eclipse.microprofile.openapi.annotations.media.Schema


@Schema(description = "Detailed information about the error")
data class ErrorDetails(
    @field:Schema(description = "The field that caused the error", example = "email")
    val field: String? = null,

    @field:Schema(description = "The specific constraint that was violated", example = "email_format")
    val constraint: String? = null,

    @field:Schema(description = "Map of constraint details", implementation = Map::class)
    val constraints: Map<String, Any>? = null,

    @field:Schema(description = "The invalid value that was provided", example = "invalid.email@")
    val value: Any? = null
)
