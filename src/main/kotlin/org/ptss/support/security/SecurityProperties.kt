package org.ptss.support.security

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class SecurityProperties @Inject constructor(
    @ConfigProperty(name = "ACCESS_TOKEN_COOKIE_NAME", defaultValue = "access_token")
    val accessTokenCookieName: String
)