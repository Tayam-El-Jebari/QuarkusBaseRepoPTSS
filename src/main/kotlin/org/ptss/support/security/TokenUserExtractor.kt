package org.ptss.support.security

import io.quarkus.logging.Log
import io.quarkus.security.UnauthorizedException
import io.smallrye.jwt.auth.principal.JWTParser
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.enums.Role
import org.ptss.support.security.context.UserContext
import io.smallrye.jwt.auth.principal.JWTAuthContextInfo
import jakarta.json.*
import org.eclipse.microprofile.jwt.JsonWebToken
import org.ptss.support.security.jwt.SimpleJsonWebToken
import java.util.Optional
import java.util.UUID
import java.util.Base64

@ApplicationScoped
class TokenUserExtractor @Inject constructor(
    private val jwtParser: JWTParser
) {
    fun extractUserContext(token: String, keycloakPublicKey: Optional<String>, jwtValidationEnabled: Boolean): Result<UserContext> {
        return runCatching {
            validateConfiguration(jwtValidationEnabled, keycloakPublicKey)
            val jwt = parseJWT(token, keycloakPublicKey, jwtValidationEnabled)
            createUserContext(jwt)
        }.recoverCatching { error ->
            when (error) {
                is APIException -> throw error
                else -> throw UnauthorizedException(
                    "Failed to verify token",
                )
            }
        }
    }
    private fun validateConfiguration(jwtValidationEnabled: Boolean, keycloakPublicKey: Optional<String>) {
        if (jwtValidationEnabled && keycloakPublicKey.isEmpty) {
            throw APIException(
                errorCode = ErrorCode.INTERNAL_ERROR,
                message = "Keycloak public key is required for JWT validation"
            )
        }
    }

    private fun parseJWT(token: String, keycloakPublicKey: Optional<String>, jwtValidationEnabled: Boolean): JsonWebToken {
        if (jwtValidationEnabled) {
            val authContext = JWTAuthContextInfo().apply {
                publicKeyContent = keycloakPublicKey.orElse("")
            }
            return jwtParser.parse(token, authContext)
        }

        Log.info("JWT validation is disabled")
        return parseUnvalidatedJWT(token)
    }

    private fun parseUnvalidatedJWT(token: String): JsonWebToken {
        val parts = token.split(".")
        if (parts.size != 3) {
            throw UnauthorizedException("Invalid JWT format: expected 3 parts but got ${parts.size}")
        }

        return try {
            val payload = String(Base64.getDecoder().decode(parts[1]))
            val claims = Json.createReader(payload.byteInputStream()).readObject()
            SimpleJsonWebToken(token, claims)
        } catch (e: IllegalArgumentException) {
            throw UnauthorizedException("Invalid JWT base64 encoding", e)
        } catch (e: JsonException) {
            throw UnauthorizedException("Invalid JWT payload JSON", e)
        }
    }



    private fun createUserContext(jwt: JsonWebToken): UserContext {
        val userId = extractUserId(jwt)
        return UserContext(
            userId = userId,
            groupId = extractGroupId(jwt),
            roles = extractRole(jwt),
            hasPin = extractHasPin(jwt)
        )
    }

    private fun extractUserId(jwt: JsonWebToken): UUID =
        jwt.getClaim<String>("user_id")
            ?.let { runCatching { UUID.fromString(it) }.getOrNull() }
            ?: throw UnauthorizedException()

    private fun extractGroupId(jwt: JsonWebToken): UUID? =
        jwt.getClaim<String>("group_id")?.let { runCatching { UUID.fromString(it) }.getOrNull() }

    private fun extractRole(jwt: JsonWebToken): Set<Role> {
        val specificRole = jwt.getClaim<String>("role")?.let {
            runCatching { Role.fromString(it.trim()) }.getOrNull()
        }

        // Then, extract any system roles from the "roles" array
        val systemRoles = when (val rolesClaim = jwt.getClaim<Any>("roles")) {
            is List<*> -> rolesClaim
            is JsonValue -> when (rolesClaim.valueType) {
                JsonValue.ValueType.ARRAY -> (rolesClaim as JsonArray).map { it.toString() }
                else -> emptyList()
            }
            else -> emptyList()
        }.mapNotNull { role ->
            val cleanRole = role.toString().trim('"').trim()
            runCatching { Role.fromString(cleanRole) }.getOrNull()
        }

        // Combine the specific role with system roles, filtering out any nulls
        return (listOfNotNull(specificRole) + systemRoles).toSet()
    }

    private fun extractHasPin(jwt: JsonWebToken): Boolean =
        when (val pinClaim = jwt.getClaim<Any>("has_pin")) {
            is Boolean -> pinClaim
            is JsonValue -> when (pinClaim.valueType) {
                JsonValue.ValueType.TRUE -> true
                JsonValue.ValueType.FALSE -> false
                else -> false
            }
            else -> false
    }
}