package org.ptss.support.security

import io.quarkus.security.UnauthorizedException
import io.smallrye.jwt.auth.principal.JWTParser
import io.smallrye.jwt.build.Jwt
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.domain.enums.Role
import java.util.Date

@ApplicationScoped
class IdentityServiceClient @Inject constructor(
    private val jwtParser: JWTParser
) {

    fun getAccessToken(role: Role = Role.PATIENT): String {
        return generateJwt(role, isRefreshToken = false)
    }

    fun getRefreshToken(role: Role = Role.PATIENT): String {
        return generateJwt(role, isRefreshToken = true)
    }

    fun getNewAccessToken(refreshToken: String): String {
        // Parse and validate the refresh token
        val claims = try {
            jwtParser.parse(refreshToken)
        } catch (e: Exception) {
            throw UnauthorizedException("Invalid refresh token")
        }

        val roleClaim = claims.getClaim("role") as String?
        val role = mapClaimToRole(roleClaim)
        return getAccessToken(role)
    }

    private fun mapClaimToRole(roleClaim: String?): Role {
        return when (roleClaim?.uppercase()) {
            "ADMIN" -> Role.ADMIN
            "PATIENT" -> Role.PATIENT
            "HCP" -> Role.HCP
            "FAMILY_MEMBER" -> Role.FAMILY_MEMBER
            "PRIMARY_CAREGIVER" -> Role.PRIMARY_CAREGIVER
            else -> throw UnauthorizedException("Invalid role in token")
        }
    }

    private fun generateJwt(role: Role, isRefreshToken: Boolean): String {
        val now = Date()
        val expiration = if (isRefreshToken) REFRESH_TOKEN_EXPIRATION else ACCESS_TOKEN_EXPIRATION
        val expirationDate = Date(now.time + expiration)

        return Jwt.issuer("https://example.com")
            .subject("user")
            .claim("role", role.name)
            .claim("type", if (isRefreshToken) "refresh" else "access")
            .issuedAt(now.toInstant())
            .expiresAt(expirationDate.toInstant())
            .sign()
    }

    companion object {
        private const val ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000L // 15 minutes
        private const val REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000L // 7 days
    }
}
