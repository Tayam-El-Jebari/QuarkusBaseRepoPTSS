package org.ptss.support.infrastructure.util

import io.quarkus.security.UnauthorizedException
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.container.ContainerRequestContext
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class CookieUtil @Inject constructor(
    @ConfigProperty(name = "ACCESS_TOKEN_COOKIE_NAME", defaultValue = "access_token")
    private val accessTokenCookieName: String
) {
    fun getAccessTokenFromCookie(requestContext: ContainerRequestContext): String {
        val token = requestContext.cookies[accessTokenCookieName]?.value
            ?: throw UnauthorizedException("Access token not found in cookies")

        if (token.isBlank()) {
            throw UnauthorizedException("Access token is blank")
        }

        return token
    }
}