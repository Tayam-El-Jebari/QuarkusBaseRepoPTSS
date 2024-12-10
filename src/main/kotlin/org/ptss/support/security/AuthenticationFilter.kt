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

        when {
            accessToken.isNullOrBlank() -> denyRequest(requestContext, annotation.message)
            jwtValidator.isAccessTokenValid(accessToken, annotation.roles) -> return
            !refreshToken.isNullOrBlank() -> handleRefreshToken(requestContext, refreshToken, annotation)
            else -> denyRequest(requestContext, annotation.message)
        }
    }

    private fun handleRefreshToken(
        requestContext: ContainerRequestContext,
        refreshToken: String,
        annotation: Authentication
    ) {
        runCatching {
            val newAccessToken = identityServiceClient.getNewAccessToken(refreshToken)
            val newAccessTokenCookie = createNewAccessTokenCookie(newAccessToken)
            requestContext.headers.add(HttpHeaders.SET_COOKIE, newAccessTokenCookie.toString())
        }.getOrElse {
            denyRequest(requestContext, annotation.message)
        }
    }

    private fun createNewAccessTokenCookie(newAccessToken: String): NewCookie =
        NewCookie.Builder(accessTokenCookieName)
            .value(newAccessToken)
            .path("/")
            .httpOnly(true)
            .secure(true)
            .build()

    private fun getAuthenticationAnnotation(resourceInfo: ResourceInfo): Authentication? =
        resourceInfo.resourceMethod.getAnnotation(Authentication::class.java)
            ?: resourceInfo.resourceClass.getAnnotation(Authentication::class.java)

    private fun denyRequest(requestContext: ContainerRequestContext, message: String) {
        requestContext.abortWith(
            Response.status(Response.Status.UNAUTHORIZED)
                .entity(message)
                .build()
        )
    }
}
