package org.ptss.support.security

import io.smallrye.jwt.auth.principal.JWTParser
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.domain.enums.Role

@ApplicationScoped
class JwtValidator @Inject constructor(
    private val jwtParser: JWTParser
) {
    fun isTokenValidAndNotBlank(token: String?): Boolean =
        !token.isNullOrBlank() && isTokenValid(token)

    fun hasRequiredRole(token: String, allowedRoles: Set<Role>): Boolean =
        extractRoleFromToken(token) in allowedRoles

    fun isTokenValid(token: String): Boolean {
        return runCatching { jwtParser.parse(token) }
            .isSuccess
    }

    fun extractRoleFromToken(token: String): Role {
        val roleClaim = extractClaim(token, "role") as String
        return Role.fromString(roleClaim)
    }

    fun extractClaim(token: String, claimName: String): Any? =
        jwtParser.parse(token).getClaim(claimName)
}

