package org.ptss.support.security

import io.quarkus.security.UnauthorizedException
import io.smallrye.jwt.auth.principal.JWTParser
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.core.mappers.RoleMapper
import org.ptss.support.domain.enums.Role

@ApplicationScoped
class JwtValidator @Inject constructor(
    private val jwtParser: JWTParser
) {
    fun isAccessTokenValid(accessToken: String, allowedRoles: Array<Role>): Boolean =
        runCatching {
            validateToken(accessToken)
            val tokenRole = extractRoleFromToken(accessToken)
            tokenRole in allowedRoles
        }.getOrElse { false }

    fun extractRoleFromToken(token: String): Role {
        val roleClaim = extractClaim(token, "role") as String?
        return RoleMapper.mapClaimToRole(roleClaim)
    }

    fun validateToken(token: String) {
        runCatching { jwtParser.parse(token) }
            .getOrElse { throw UnauthorizedException("Invalid token") }
    }

    fun extractClaim(token: String, claimName: String): Any? =
        jwtParser.parse(token).getClaim(claimName)
}
