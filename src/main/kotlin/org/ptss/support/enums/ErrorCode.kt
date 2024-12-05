package org.ptss.support.enums

enum class ErrorCode(
    val code: String,
    val status: Int = 400,
    val description: String
) {
    INVALID_TOKEN("AUTH0001", 401, "The provided authentication token is invalid or expired"),
    INVALID_REQUEST("VAL0001", 400, "The request contains invalid parameters"),
    RESOURCE_NOT_FOUND("RES0001", 404, "The requested resource could not be found"),
    INTERNAL_ERROR("SYS0001", 500, "An unexpected system error occurred");

    companion object {
        fun fromCode(code: String): ErrorCode? = values().find { it.code == code }
    }
}