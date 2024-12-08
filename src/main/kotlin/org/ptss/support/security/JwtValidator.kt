package org.ptss.support.security


import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.enums.Role
import java.security.Key

@ApplicationScoped
class JwtValidator {
    fun validateAccessToken(accessToken: String): Role {
        // Validate the access token and return the user's role
        return Role.Patient
    }

    fun validateRefreshToken(refreshToken: String): Role {
        // Validate the refresh token and return the user's role
        return Role.Patient
    }
}