package org.ptss.support.core.mappers

import io.quarkus.security.UnauthorizedException
import org.ptss.support.domain.enums.Role

object RoleMapper {
    fun mapClaimToRole(roleClaim: String?): Role =
        roleClaim?.takeUnless { it.isBlank() }
            ?.let { Role.valueOf(it.uppercase()) }
            ?: throw UnauthorizedException("Role claim is missing or invalid")
}