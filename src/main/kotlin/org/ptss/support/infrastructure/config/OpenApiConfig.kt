package org.ptss.support.infrastructure.config

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType
import org.eclipse.microprofile.openapi.annotations.info.Info
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme
import jakarta.ws.rs.core.Application

@OpenAPIDefinition(
    info = Info(
        title = "PTSS SUPPORT API",
        version = "1.0.0",
        description = "This is the ptss support api"
    ),
)
@SecurityScheme(
    description = "JWT Authorization header using the Bearer scheme.",
    scheme = "bearer",
    bearerFormat = "JWT",
    type = SecuritySchemeType.HTTP
)
class OpenAPIConfig : Application()