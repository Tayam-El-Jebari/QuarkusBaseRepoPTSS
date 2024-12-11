package org.ptss.support.security


import io.quarkus.security.UnauthorizedException
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.container.ResourceInfo
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.HttpHeaders
import jakarta.ws.rs.core.NewCookie
import jakarta.ws.rs.ext.Provider


@Provider
@ApplicationScoped
class AuthenticationFilter @Inject constructor(
    @Context private val resourceInfo: ResourceInfo,
    private val identityServiceClient: IdentityServiceClient,
    private val jwtValidator: JwtValidator,
    private val securityProperties: SecurityProperties
) : ContainerRequestFilter {

    companion object {
        private const val AUTHENTICATION_FAILED_MESSAGE = "Authentication failed"
    }

    override fun filter(requestContext: ContainerRequestContext) {
        val annotation = getAuthenticationAnnotation(resourceInfo) ?: return

        val accessToken = requestContext.cookies[securityProperties.accessTokenCookieName]?.value
        val refreshToken = requestContext.cookies[securityProperties.refreshTokenCookieName]?.value

        // Validate refresh token
        if (!jwtValidator.isTokenValidAndNotBlank(refreshToken)) {
            throw UnauthorizedException(AUTHENTICATION_FAILED_MESSAGE)
        }

        // Determine token to use
        val tokenToUse = when {
            jwtValidator.isTokenValidAndNotBlank(accessToken) -> accessToken!!
            else -> refreshAccessToken(requestContext, refreshToken!!)
        }

        // Check role authorization
        if (!jwtValidator.hasRequiredRole(tokenToUse, annotation.roles.toSet())) {
            throw UnauthorizedException(AUTHENTICATION_FAILED_MESSAGE)
        }
    }

    private fun refreshAccessToken(
        requestContext: ContainerRequestContext,
        refreshToken: String
    ): String {
        return try {
            val newToken = identityServiceClient.refreshAccessToken(refreshToken)
            val newAccessTokenCookie = createNewAccessTokenCookie(newToken)
            requestContext.headers.add(HttpHeaders.SET_COOKIE, newAccessTokenCookie.toString())
            newToken
        } catch (e: Exception) {
            throw UnauthorizedException(AUTHENTICATION_FAILED_MESSAGE)
        }
    }

    private fun createNewAccessTokenCookie(newAccessToken: String): NewCookie =
        NewCookie.Builder(securityProperties.accessTokenCookieName)
            .value(newAccessToken)
            .path(securityProperties.accessTokenCookiePath)
            .domain(securityProperties.accessTokenCookieDomain)
            .maxAge(securityProperties.accessTokenCookieMaxAge)
            .httpOnly(securityProperties.accessTokenCookieHttpOnly)
            .secure(securityProperties.accessTokenCookieSecure)
            .build()

    private fun getAuthenticationAnnotation(resourceInfo: ResourceInfo): Authentication? =
        resourceInfo.resourceMethod?.getAnnotation(Authentication::class.java)
            ?: resourceInfo.resourceClass?.getAnnotation(Authentication::class.java)
}

