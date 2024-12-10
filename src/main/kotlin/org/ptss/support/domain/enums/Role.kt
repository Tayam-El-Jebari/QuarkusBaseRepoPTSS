package org.ptss.support.domain.enums

import io.quarkus.security.UnauthorizedException

enum class Role {
    ADMIN, PATIENT, HCP, FAMILY_MEMBER, PRIMARY_CAREGIVER;

    companion object {
        fun fromString(role: String?): Role {
            return role?.takeUnless { it.isBlank() }
                ?.let { valueOf(it.uppercase()) }
                ?: throw UnauthorizedException("Role claim is missing or invalid")
        }
    }
}