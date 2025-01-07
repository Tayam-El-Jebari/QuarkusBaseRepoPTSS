package org.ptss.support.security

import jakarta.ws.rs.NameBinding
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.ptss.support.domain.enums.Role

@NameBinding
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@SecurityRequirement(name = "bearer-token")
annotation class Authentication(
    val roles: Array<Role>,
    val message: String = "Unauthorized access"
)