package org.ptss.support.core.context

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.container.ContainerResponseContext
import jakarta.ws.rs.container.ContainerResponseFilter
import jakarta.ws.rs.ext.Provider
import java.util.UUID

@Provider
@ApplicationScoped
class RequestContextFilter @Inject constructor(
    private val requestContextService: RequestContextService
) : ContainerRequestFilter, ContainerResponseFilter {

    override fun filter(requestContext: ContainerRequestContext) {
        val requestId = requestContext.headers.getFirst("X-Request-ID")
            ?: UUID.randomUUID().toString()

        requestContextService.setContext(
            requestId = requestId,
            path = requestContext.uriInfo.path
        )
        requestContext.headers.putSingle("X-Request-ID", requestId)
    }

    override fun filter(
        requestContext: ContainerRequestContext,
        responseContext: ContainerResponseContext
    ) {
        responseContext.headers.putSingle("X-Request-ID", requestContextService.getCurrentRequestId())
        requestContextService.clearContext()
    }
}