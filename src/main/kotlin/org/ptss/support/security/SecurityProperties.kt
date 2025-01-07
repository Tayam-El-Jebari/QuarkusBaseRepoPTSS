package org.ptss.support.security

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.util.Optional

@ApplicationScoped
class SecurityProperties @Inject constructor(
    @ConfigProperty(name = "ACCESS_TOKEN_COOKIE_NAME", defaultValue = "access_token")
    val accessTokenCookieName: String,

    @ConfigProperty(name = "KEYCLOAK_PUBLIC_KEY")
    val keycloakPublicKey: Optional<String>,

    @ConfigProperty(name = "JWT_VALIDATION_ENABLED", defaultValue = "true")
    val jwtValidationEnabled: Boolean
)