package org.ptss.support.domain.constants

// Constants for Token Expiration
object TokenExpiration {
    const val ACCESS_TOKEN_DURATION = 15 * 60 * 1000L // 15 minutes
    const val REFRESH_TOKEN_DURATION = 7 * 24 * 60 * 60 * 1000L // 7 days
}
