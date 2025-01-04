package org.ptss.support.security


import io.quarkus.logging.Log
import io.quarkus.security.UnauthorizedException
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.container.ResourceInfo
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.ext.Provider

@Provider
@ApplicationScoped
class AuthenticationFilter @Inject constructor(
    @Context private val resourceInfo: ResourceInfo,
    private val tokenUserExtractor: TokenUserExtractor,
    private val securityProperties: SecurityProperties
) : ContainerRequestFilter {

    companion object {
        private const val AUTHENTICATION_FAILED_MESSAGE = "Authentication failed"
    }

    override fun filter(requestContext: ContainerRequestContext) {
        val annotation = getAuthenticationAnnotation(resourceInfo) ?: return

        val accessToken = runCatching {
            getAccessToken(requestContext)
        }.getOrNull() ?: throw UnauthorizedException(AUTHENTICATION_FAILED_MESSAGE)

        val userRoles = tokenUserExtractor.getUserRoles(accessToken)
        if (userRoles.isEmpty()) {
            Log.error("Failed to extract roles or token expired")
            throw UnauthorizedException(AUTHENTICATION_FAILED_MESSAGE)
        }

        val requiredRoles = annotation.roles.toSet()
        if (requiredRoles.isNotEmpty() && userRoles.none { it in requiredRoles }) {
            Log.warn("User does not have required roles for request")
            throw UnauthorizedException(AUTHENTICATION_FAILED_MESSAGE)
        }
    }

    private fun getAccessToken(requestContext: ContainerRequestContext): String {
        val token = requestContext.cookies[securityProperties.accessTokenCookieName]?.value
            ?: throw UnauthorizedException("Access token not found in cookies")

        if (token.isBlank()) {
            throw UnauthorizedException("Access token is blank")
        }

        return token
    }

    private fun getAuthenticationAnnotation(resourceInfo: ResourceInfo): Authentication? =
        resourceInfo.resourceMethod?.getAnnotation(Authentication::class.java)
            ?: resourceInfo.resourceClass?.getAnnotation(Authentication::class.java)
}

