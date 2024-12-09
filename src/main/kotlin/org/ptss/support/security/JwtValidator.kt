package org.ptss.support.security

import io.quarkus.security.UnauthorizedException
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.enums.Role

@ApplicationScoped
class JwtValidator {
    fun validateAccessToken(accessToken: String, allowedRoles: Array<Role>): Role {
        return when (accessToken) {
            //"patient_token" -> Role.Patient
            "admin_token" -> Role.Admin
            //"hcp_token" -> Role.HCP
            else -> throw UnauthorizedException("Invalid token")
        }.takeIf { it in allowedRoles }
            ?: throw UnauthorizedException("Insufficient permissions")
    }

    fun validateRefreshToken(refreshToken: String): Role {
        return when (refreshToken) {
            //"patient_refresh_token" -> Role.Patient
            "admin_refresh_token" -> Role.Admin
            //"hcp_refresh_token" -> Role.HCP
            else -> throw UnauthorizedException("Invalid refresh token")
        }
    }
}