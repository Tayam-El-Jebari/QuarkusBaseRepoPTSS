package org.ptss.support.security

import io.quarkus.security.UnauthorizedException
import io.smallrye.jwt.auth.principal.JWTParser
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.domain.enums.Role

@ApplicationScoped
class JwtValidator @Inject constructor(
    private val jwtParser: JWTParser
) {

    fun validateAccessToken(accessToken: String, allowedRoles: Array<Role>): Role {
        return try {
            val claims = jwtParser.parse(accessToken)
            val tokenRole = mapClaimToRole(claims.getClaim("role") as String?)

            tokenRole.takeIf { it in allowedRoles }
                ?: throw UnauthorizedException("Insufficient permissions")
        } catch (e: Exception) {
            throw UnauthorizedException("Invalid access token")
        }
    }

    fun validateRefreshToken(refreshToken: String): Role {
        return try {
            val claims = jwtParser.parse(refreshToken)
            mapClaimToRole(claims.getClaim("role") as String?)
        } catch (e: Exception) {
            throw UnauthorizedException("Invalid refresh token")
        }
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
}