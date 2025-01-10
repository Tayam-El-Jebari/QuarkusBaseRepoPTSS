package org.ptss.support.security.jwt

import jakarta.json.*
import org.eclipse.microprofile.jwt.JsonWebToken

class SimpleJsonWebToken(
    private val token: String,
    private val claims: JsonObject
) : JsonWebToken {
    override fun <T> getClaim(claimName: String): T? {
        val value = claims.get(claimName) ?: return null
        @Suppress("UNCHECKED_CAST")
        return when (value) {
            is JsonString -> value.string as? T
            is JsonNumber -> value.numberValue() as? T
            is JsonArray -> value.toString() as? T
            is JsonValue -> value.toString() as? T
            else -> value as? T
        }
    }

    override fun getClaimNames(): MutableSet<String> = claims.keys
    override fun getRawToken(): String = token
    override fun getIssuer(): String? = getClaim("iss")
    override fun getGroups(): MutableSet<String> = mutableSetOf()
    override fun getName(): String = getClaim("sub") ?: ""
}