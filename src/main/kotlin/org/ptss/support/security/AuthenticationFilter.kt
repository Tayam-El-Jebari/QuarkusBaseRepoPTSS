package org.ptss.support.security


import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.container.ResourceInfo
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.Response
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
            val accessToken = requestContext.getHeaderString("Authorization")
            val refreshToken = requestContext.getCookies()["refresh_token"]?.value

            if (accessToken != null && refreshToken != null) {
                val accessTokenRole = jwtValidator.validateAccessToken(accessToken, annotation.roles)
                val refreshTokenRole = jwtValidator.validateRefreshToken(refreshToken)

                if (accessTokenRole in annotation.roles) {
                    // Access token is valid, allow request to proceed
                    return
                } else if (refreshTokenRole in annotation.roles) {
                    // Access token is invalid, but refresh token is valid
                    // Get a new access token from the IdentityServiceClient
                    val newAccessToken = identityServiceClient.getNewAccessToken(refreshToken)
                    requestContext.headers.putSingle("Authorization", newAccessToken)
                    return
                }
            }

            // Both access token and refresh token are invalid
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                    .entity(annotation.message)
                    .build()
            )
        }
    }
}