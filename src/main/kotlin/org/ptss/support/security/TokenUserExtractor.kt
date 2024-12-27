package org.ptss.support.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.domain.enums.Role
import java.util.Base64

@ApplicationScoped
class TokenUserExtractor @Inject constructor(
) {
    private val objectMapper = ObjectMapper()
    fun getUserId(token: String): String? {
        return runCatching {
            val parts = token.split(".")
            if (parts.size != 3) return null

            val payload = String(Base64.getUrlDecoder().decode(parts[1]))
            val jsonObject = objectMapper.readTree(payload)

            jsonObject.get("user_id")?.asText()
        }.getOrNull()
    }

    fun getUserRoles(token: String): Set<Role> {
        return runCatching {
            val parts = token.split(".")
            if (parts.size != 3) return emptySet()

            val payload = String(Base64.getUrlDecoder().decode(parts[1]))
            val jsonObject = objectMapper.readTree(payload)

            val rolesNode = jsonObject.get("roles")
            if (!rolesNode.isArray) return emptySet()

            rolesNode.mapNotNull { role ->
                runCatching { Role.fromString(role.asText()) }.getOrNull()
            }.toSet()
        }.getOrElse { emptySet() }
    }
}