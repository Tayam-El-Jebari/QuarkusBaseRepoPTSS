package org.ptss.support.domain.enums

enum class ErrorCode(
    val code: String,
    val status: Int,
    val description: String
) {
    // Authentication errors
    INVALID_TOKEN("INVALID_TOKEN", 401, "Invalid or expired authentication token"),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", 401, "Invalid email or password provided"),
    EXPIRED_TOKEN("EXPIRED_TOKEN", 401, "Authentication token has expired"),
    TOKEN_GENERATION_FAILED("TOKEN_GENERATION_FAILED", 401, "Failed to generate a valid access token"),

    // Authorization errors
    INSUFFICIENT_PERMISSIONS("INSUFFICIENT_PERMISSIONS", 403, "User does not have required permissions"),

    // Validation errors
    VALIDATION_ERROR("VALIDATION_ERROR", 400, "Request validation failed"),
    DUPLICATE_ENTRY("DUPLICATE_ENTRY", 400, "Resource already exists"),

    // Resource errors
    USER_NOT_FOUND("USER_NOT_FOUND", 404, "The requested user was not found"),
    GROUP_NOT_FOUND("GROUP_NOT_FOUND", 404, "The specified group does not exist"),
    NOT_FOUND("NOT_FOUND", 404, "The requested resource was not found"),

    INTERNAL_ERROR("INTERNAL_ERROR", 500, "An unexpected system error occurred"),
    RATE_LIMIT_EXCEEDED("RATE_LIMIT_EXCEEDED", 429, "Too many requests"),
    SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE", 503, "Service temporarily unavailable"),
    GATEWAY_TIMEOUT("GATEWAY_TIMEOUT", 504, "Request timed out"),

    // Product errors
    PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND", 404, "The requested product was not found"),
    PRODUCT_VALIDATION_ERROR("PRODUCT_VALIDATION_ERROR", 400, "Product data validation failed"),
    PRODUCT_CREATION_ERROR("PRODUCT_CREATION_ERROR", 400, "Failed to create product");

    companion object {
        fun fromCode(code: String): ErrorCode? = values().find { it.code == code }
    }
}