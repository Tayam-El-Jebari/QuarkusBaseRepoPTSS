package org.ptss.support.security


import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.container.ResourceInfo
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.ext.Provider

@Provider
@ApplicationScoped
class AuthenticationFilter(
    @Context private val resourceInfo: ResourceInfo,
    private val identityServiceClient: IdentityServiceClient,
    private val jwtValidator: JwtValidator
) : ContainerRequestFilter {

    override fun filter(requestContext: ContainerRequestContext) {
        val annotation = resourceInfo.resourceMethod.getAnnotation(Authentication::class.java)
            ?: resourceInfo.resourceClass.getAnnotation(Authentication::class.java)

        if (annotation != null) {
            val accessToken = identityServiceClient.getAccessToken()
            val role = jwtValidator.validateAccessToken(accessToken)

            if (role !in annotation.roles) {
                // Throw appropriate error
            }
        }
    }
}