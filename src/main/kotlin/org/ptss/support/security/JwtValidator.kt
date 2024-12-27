package org.ptss.support.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.domain.enums.Role
import java.util.Base64

@ApplicationScoped
class JwtValidator @Inject constructor(
    private val tokenUserExtractor: TokenUserExtractor
) {

    fun isTokenValid(token: String): Boolean {
        return runCatching {
            // Decode the JWT
            val parts = token.split(".")
            if (parts.size != 3) return false

            val payload = String(Base64.getUrlDecoder().decode(parts[1]))
            val jsonObject = ObjectMapper().readTree(payload)

            // Check expiration
            val exp = jsonObject.get("exp").asLong()
            val now = System.currentTimeMillis() / 1000

            exp > now
        }.getOrElse { false }
    }

    fun hasRequiredRole(token: String, allowedRoles: Set<Role>): Boolean =
        tokenUserExtractor.getUserRoles(token).any { it in allowedRoles }
}
