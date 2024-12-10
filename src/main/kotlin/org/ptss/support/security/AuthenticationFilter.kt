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
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.Provider
import org.eclipse.microprofile.config.inject.ConfigProperty


@Provider
@ApplicationScoped
class AuthenticationFilter @Inject constructor(
    @Context private val resourceInfo: ResourceInfo,
    private val identityServiceClient: IdentityServiceClient,
    private val jwtValidator: JwtValidator,
    @ConfigProperty(name = "app.security.access-token-cookie-name", defaultValue = "access_token")
    private val accessTokenCookieName: String,
    @ConfigProperty(name = "app.security.refresh-token-cookie-name", defaultValue = "refresh_token")
    private val refreshTokenCookieName: String
) : ContainerRequestFilter {

    override fun filter(requestContext: ContainerRequestContext) {
        val annotation = getAuthenticationAnnotation(resourceInfo) ?: return
        val accessToken = requestContext.cookies[accessTokenCookieName]?.value
        val refreshToken = requestContext.cookies[refreshTokenCookieName]?.value

        if (!jwtValidator.isTokenValidAndNotBlank(refreshToken)) {
            denyRequest(requestContext, annotation.message)
        }

        val tokenToUse = when {
            jwtValidator.isTokenValidAndNotBlank(accessToken) -> accessToken!!
            else -> tryRefreshAccessToken(requestContext, refreshToken!!)
        }

        if (!jwtValidator.hasRequiredRole(tokenToUse, annotation.roles.toSet())) {
            denyRequest(requestContext, annotation.message)
        }

        return
    }

    private fun tryRefreshAccessToken(
        requestContext: ContainerRequestContext,
        refreshToken: String
    ): String = runCatching {
        val newToken = identityServiceClient.refreshAccessToken(refreshToken)
        val newAccessTokenCookie = createNewAccessTokenCookie(newToken)
        requestContext.headers.add(HttpHeaders.SET_COOKIE, newAccessTokenCookie.toString())
        newToken
    }.getOrElse {
        denyRequest(requestContext, "Unauthorized")
        throw UnauthorizedException("Failed to refresh token")
    }

    private fun createNewAccessTokenCookie(newAccessToken: String): NewCookie =
        NewCookie.Builder(accessTokenCookieName)
            .value(newAccessToken)
            .path("/")
            .httpOnly(true)
            .secure(true)
            .build()

    private fun denyRequest(requestContext: ContainerRequestContext, message: String) {
        requestContext.abortWith(
            Response.status(Response.Status.UNAUTHORIZED)
                .entity(message)
                .build()
        )
    }

    private fun getAuthenticationAnnotation(resourceInfo: ResourceInfo): Authentication? =
        resourceInfo.resourceMethod.getAnnotation(Authentication::class.java)
            ?: resourceInfo.resourceClass.getAnnotation(Authentication::class.java)

}

