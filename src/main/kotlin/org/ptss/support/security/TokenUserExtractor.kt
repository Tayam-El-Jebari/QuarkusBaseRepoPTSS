package org.ptss.support.security

import io.smallrye.jwt.auth.principal.JWTParser
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.jwt.JsonWebToken
import org.ptss.support.domain.enums.Role
import org.ptss.support.security.context.UserContext
import java.time.Instant
import java.util.UUID

@ApplicationScoped
class TokenUserExtractor @Inject constructor(
    private val jwtParser: JWTParser
) {
    fun extractUserContext(token: String): Result<UserContext> {
        return runCatching {
            val jwt = jwtParser.parse(token)
            if (isExpired(jwt)) {
                return Result.failure(Exception("Token expired"))
            }

            val userId = jwt.getClaim<String>("user_id")?.let { runCatching { UUID.fromString(it) }.getOrNull() }
                ?: return Result.failure(Exception("Invalid or missing user_id in token"))

            val groupId = jwt.getClaim<String>("group_id")?.let { runCatching { UUID.fromString(it) }.getOrNull() }

            UserContext(
                userId = userId,
                groupId = groupId,
                roles = jwt.groups.mapNotNull { runCatching { Role.fromString(it) }.getOrNull() }.toSet(),
                hasPin = jwt.getClaim("has_pin") ?: false
            )
        }
    }

    // basic safety measure for expired tokens when service-to-service calls using the same token expires midway through
    private fun isExpired(jwt: JsonWebToken): Boolean {
        return jwt.expirationTime < Instant.now().epochSecond
    }
}