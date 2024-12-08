package org.ptss.support.security

import jakarta.ws.rs.NameBinding
import org.ptss.support.domain.enums.Role


@NameBinding
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Authentication(
    val roles: Array<Role>,
    val message: String = "Unauthorized access"
)
