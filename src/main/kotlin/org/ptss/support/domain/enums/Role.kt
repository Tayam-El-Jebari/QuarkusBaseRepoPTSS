package org.ptss.support.domain.enums

enum class Role {
    ADMIN, PATIENT, HEALTHCARE_PROFESSIONAL, FAMILY_MEMBER, PRIMARY_CAREGIVER;

    companion object {
        fun fromString(value: String): Role =
            runCatching { valueOf(value.uppercase()) }
                .getOrElse { throw IllegalArgumentException("Invalid role: $value. Valid values are: ${values().joinToString()}") }
    }
}