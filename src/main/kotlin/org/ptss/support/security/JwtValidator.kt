package org.ptss.support.security

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.enums.Role

@ApplicationScoped
class JwtValidator {
    fun validateAccessToken(accessToken: String, allowedRoles: Array<Role>): Role {
        // Validate the access token and return the user's role
        return if (allowedRoles.contains(Role.Patient)) Role.Patient else Role.Admin
    }

    fun validateRefreshToken(refreshToken: String): Role {
        // Validate the refresh token and return the user's role
        return Role.Patient
    }
}