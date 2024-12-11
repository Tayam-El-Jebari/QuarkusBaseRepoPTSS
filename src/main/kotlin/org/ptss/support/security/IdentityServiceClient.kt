package org.ptss.support.security

import io.smallrye.jwt.build.Jwt
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.constants.TokenExpiration
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.enums.Role
import java.util.Date

@ApplicationScoped
class IdentityServiceClient @Inject constructor(
    private val jwtValidator: JwtValidator
) {
    fun refreshAccessToken(refreshToken: String): String {
        val newToken = getNewAccessToken(refreshToken)
        if (!jwtValidator.isTokenValidAndNotBlank(newToken)) {
            throw APIException(
                errorCode = ErrorCode.TOKEN_GENERATION_FAILED,
                message = "Failed to generate valid access token"
            )
        }
        return newToken
    }

    private fun getNewAccessToken(refreshToken: String): String {
        val roleClaim = runCatching { jwtValidator.extractClaim(refreshToken, "role") as String }
            .getOrElse {throw APIException(
                errorCode = ErrorCode.INVALID_TOKEN,
                message = "Invalid refresh token"
            ) }

        val role = Role.fromString(roleClaim)
        return generateJwt(role, isRefreshToken = false)
    }

    private fun generateJwt(role: Role, isRefreshToken: Boolean): String {
        val now = Date()
        val expiration = if (isRefreshToken)
            TokenExpiration.REFRESH_TOKEN_DURATION
        else
            TokenExpiration.ACCESS_TOKEN_DURATION

        val expirationDate = Date(now.time + expiration)

        return Jwt.issuer("https://example.com")
            .subject("user")
            .claim("role", role.name)
            .claim("type", if (isRefreshToken) "refresh" else "access")
            .issuedAt(now.toInstant())
            .expiresAt(expirationDate.toInstant())
            .sign()
    }
}

