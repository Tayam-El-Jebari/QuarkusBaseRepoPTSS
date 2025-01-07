package org.ptss.support.security.context

import io.quarkus.security.UnauthorizedException
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
class AuthenticatedUserContext @Inject constructor() {
    private val userContext = ThreadLocal<UserContext>()

    fun getCurrentUser(): UserContext = userContext.get()
        ?: throw UnauthorizedException("No authenticated user context found")

    internal fun setCurrentUser(context: UserContext) {
        userContext.set(context)
    }

    internal fun clearCurrentUser() {
        userContext.remove()
    }
}