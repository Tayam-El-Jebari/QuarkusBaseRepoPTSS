package org.ptss.support.enums

enum class ErrorCode(val code: String, val status: Int = 400) {
    INVALID_TOKEN("AUTH0001", 401),
}