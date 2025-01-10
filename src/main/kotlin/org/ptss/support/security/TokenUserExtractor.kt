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
import jakarta.json.JsonArray
import jakarta.json.JsonNumber
import jakarta.json.JsonString
import jakarta.json.JsonValue
import org.eclipse.microprofile.jwt.JsonWebToken
import java.util.Optional
import java.util.UUID

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
                    "Failed to verify token" + error.message,
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
        } else {
            Log.info("JWT validation is disabled")
            val parts = token.split(".")
            if (parts.size != 3) throw UnauthorizedException("Invalid JWT format")

            val payload = String(java.util.Base64.getDecoder().decode(parts[1]))
            val claims = jakarta.json.Json.createReader(payload.byteInputStream()).readObject()

            return object : JsonWebToken {
                override fun <T> getClaim(claimName: String): T? {
                    val value = claims.get(claimName)
                    return when {
                        value == null -> null
                        value is JsonString -> value.string as? T
                        value is JsonNumber -> value.numberValue() as? T
                        value is JsonArray -> value.toString() as? T
                        value is JsonValue -> value.toString() as? T
                        else -> value as? T
                    }
                }

                override fun getClaimNames(): MutableSet<String> = claims.keys

                override fun getRawToken(): String = token
                override fun getIssuer(): String? = getClaim("iss")
                override fun getGroups(): MutableSet<String> = mutableSetOf()
                override fun getName(): String = getClaim("sub") ?: ""
            }
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