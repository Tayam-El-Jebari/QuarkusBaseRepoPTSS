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
import org.ptss.support.domain.enums.Role
import org.ptss.support.security.context.AuthenticatedUserContext
import org.ptss.support.security.context.UserContext

@Provider
@ApplicationScoped
class AuthenticationFilter @Inject constructor(
    @Context private val resourceInfo: ResourceInfo,
    private val tokenUserExtractor: TokenUserExtractor,
    private val securityProperties: SecurityProperties,
    private val userContext: AuthenticatedUserContext
) : ContainerRequestFilter {

    companion object {
        private const val AUTHENTICATION_FAILED_MESSAGE = "Authentication failed"
    }

    override fun filter(requestContext: ContainerRequestContext) {
        val annotation = getAuthenticationAnnotation(resourceInfo) ?: return

        val accessToken = runCatching {
            getAccessToken(requestContext)
        }.getOrNull() ?: run {
            Log.error("Failed to get access token")
            throw UnauthorizedException(AUTHENTICATION_FAILED_MESSAGE)
        }

        val context = tokenUserExtractor.extractUserContext(accessToken, securityProperties.keycloakPublicKey,  securityProperties.jwtValidationEnabled)
            .getOrElse {
                Log.error("Failed to extract user context: ${it.message}")
                throw UnauthorizedException(AUTHENTICATION_FAILED_MESSAGE)
            }


        // Validate roles against annotation requirements
        val requiredRoles = annotation.roles.toSet()
        if (requiredRoles.isNotEmpty() && context.roles.none { it in requiredRoles }) {
            Log.warn("User does not have required roles for request")
            throw UnauthorizedException(AUTHENTICATION_FAILED_MESSAGE)
        }

        Log.info("Authenticated user ${context.userId} with roles: ${context.roles}")
        if (context.groupId != null) {
            Log.info("User belongs to group: ${context.groupId}")
        }
        Log.info("Full authentication context: $context")

        // Check if user has any valid roles at all
        if (context.roles.isEmpty()) {
            Log.error("Failed to extract roles or token expired")
            throw UnauthorizedException(AUTHENTICATION_FAILED_MESSAGE)
        }

        validateGroupIdConstraints(context)

        userContext.setCurrentUser(context)
    }

    // Just a little extra line of defense
    // See Defense in depth: https://en.wikipedia.org/wiki/Defense_in_depth_(computing)
    private fun validateGroupIdConstraints(context: UserContext) {
        val isAdminOrHEALTHCAREPROFESSIONAL = context.roles.any { it == Role.ADMIN || it == Role.HEALTHCARE_PROFESSIONAL }
        if (!isAdminOrHEALTHCAREPROFESSIONAL && context.groupId == null) {
            throw UnauthorizedException("Group ID is required for non-admin/HCP users")
        }
    }

    private fun getAccessToken(requestContext: ContainerRequestContext): String {
        val token = requestContext.cookies[securityProperties.accessTokenCookieName]?.value
        Log.debug("Token present: ${token != null}")

        if (token.isNullOrBlank()) {
            Log.error("Token not found in cookie")
            throw UnauthorizedException("Access token not found")
        }

        return token
    }

    private fun getAuthenticationAnnotation(resourceInfo: ResourceInfo): Authentication? =
        resourceInfo.resourceMethod?.getAnnotation(Authentication::class.java)
            ?: resourceInfo.resourceClass?.getAnnotation(Authentication::class.java)
}

