package org.ptss.support.security

import io.quarkus.security.UnauthorizedException
import io.smallrye.jwt.auth.principal.JWTParser
import io.smallrye.jwt.build.Jwt
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.core.mappers.RoleMapper
import org.ptss.support.domain.contants.TokenExpiration
import org.ptss.support.domain.enums.Role
import java.util.Date

@ApplicationScoped
class IdentityServiceClient @Inject constructor(
    private val jwtParser: JWTParser
) {
    fun getAccessToken(role: Role = Role.PATIENT): String =
        generateJwt(role, isRefreshToken = false)

    fun getRefreshToken(role: Role = Role.PATIENT): String =
        generateJwt(role, isRefreshToken = true)

    fun getNewAccessToken(refreshToken: String): String {
        val claims = runCatching { jwtParser.parse(refreshToken) }
            .getOrElse { throw UnauthorizedException("Invalid refresh token") }

        val roleClaim = claims.getClaim("role") as String?
        val role = RoleMapper.mapClaimToRole(roleClaim)

        return getAccessToken(role)
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
