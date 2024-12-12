package org.ptss.support.security

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty


@ApplicationScoped
class SecurityProperties @Inject constructor(
    @ConfigProperty(name = "app.security.refresh-token-cookie-name", defaultValue = "refresh_token")
    val refreshTokenCookieName: String,
    @ConfigProperty(name = "app.security.access-token-cookie-name", defaultValue = "access_token")
    val accessTokenCookieName: String,
    @ConfigProperty(name = "app.security.access-token-cookie-http-only", defaultValue = "true")
    val accessTokenCookieHttpOnly: Boolean,
    @ConfigProperty(name = "app.security.access-token-cookie-secure", defaultValue = "true")
    val accessTokenCookieSecure: Boolean,
    @ConfigProperty(name = "app.security.access-token-cookie-same-site", defaultValue = "Strict")
    val accessTokenCookieSameSite: String,
    @ConfigProperty(name = "app.security.access-token-cookie-domain", defaultValue = "")
    val accessTokenCookieDomain: String,
    @ConfigProperty(name = "app.security.access-token-cookie-path", defaultValue = "/")
    val accessTokenCookiePath: String,
    @ConfigProperty(name = "app.security.access-token-cookie-max-age", defaultValue = "3600")
    val accessTokenCookieMaxAge: Int
)