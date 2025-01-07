package org.ptss.support.security

import io.smallrye.jwt.auth.principal.JWTParser
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.enums.Role
import org.ptss.support.security.context.UserContext
import io.smallrye.jwt.auth.principal.JWTAuthContextInfo
import java.util.Optional
import java.util.UUID

@ApplicationScoped
class TokenUserExtractor @Inject constructor(
    private val jwtParser: JWTParser
) {
    fun extractUserContext(token: String, keycloakPublicKey: Optional<String>, jwtValidationEnabled: Boolean): Result<UserContext> {
        return runCatching {
            if (jwtValidationEnabled && keycloakPublicKey.isEmpty) {
                return Result.failure(
                    APIException(
                    errorCode = ErrorCode.INTERNAL_ERROR,
                    message = "Keycloak public key is required for JWT validation")
                )
            }
            val jwt = if (jwtValidationEnabled) {
                val authContext = JWTAuthContextInfo().apply {
                    publicKeyContent = keycloakPublicKey.orElse("")
                }
                jwtParser.parse(token, authContext)
            } else {
                jwtParser.parse(token)
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
}