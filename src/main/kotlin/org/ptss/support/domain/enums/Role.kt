package org.ptss.support.domain.enums

import io.quarkus.security.UnauthorizedException

enum class Role {
    ADMIN, PATIENT, HCP, FAMILY_MEMBER, PRIMARY_CAREGIVER;

    companion object {
        fun fromString(value: String): Role =
            runCatching { valueOf(value.uppercase()) }
                .getOrElse { throw IllegalArgumentException("Invalid role: $value. Valid values are: ${values().joinToString()}") }
    }
}