package org.ptss.support.security.context

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerResponseContext
import jakarta.ws.rs.container.ContainerResponseFilter
import jakarta.ws.rs.ext.Provider

@Provider
@ApplicationScoped
class ContextCleanupFilter @Inject constructor(
    private val userContext: AuthenticatedUserContext
) : ContainerResponseFilter {
    override fun filter(requestContext: ContainerRequestContext, responseContext: ContainerResponseContext) {
        userContext.clearCurrentUser()
    }
}