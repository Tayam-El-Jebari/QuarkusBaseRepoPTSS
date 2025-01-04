package org.ptss.support.security

import io.smallrye.jwt.auth.principal.JWTParser
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.jwt.JsonWebToken
import org.ptss.support.domain.enums.Role

@ApplicationScoped
class TokenUserExtractor @Inject constructor(
    private val jwtParser: JWTParser
) {
    fun getUserId(token: String): String? {
        return runCatching {
            val jwt = jwtParser.parse(token)
            if (isExpired(jwt)) return null
            jwt.subject // or jwt.getClaim("user_id")
        }.getOrNull()
    }

    fun getUserRoles(token: String): Set<Role> {
        return runCatching {
            val jwt = jwtParser.parse(token)
            if (isExpired(jwt)) return emptySet()
            jwt.groups.mapNotNull {
                runCatching { Role.fromString(it) }.getOrNull()
            }.toSet()
        }.getOrElse { emptySet() }
    }
    // basic safety measure for expired tokens when service-to-service calls using the same token expires midway through
    private fun isExpired(jwt: JsonWebToken): Boolean {
        val now = System.currentTimeMillis() / 1000
        return jwt.expirationTime < now
    }
}