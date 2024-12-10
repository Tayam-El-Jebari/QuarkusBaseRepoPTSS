package org.ptss.support.resources

import io.smallrye.faulttolerance.api.RateLimit
import jakarta.inject.Inject
import jakarta.ws.rs.Path
import jakarta.ws.rs.GET
import jakarta.ws.rs.Produces
import jakarta.ws.rs.ForbiddenException
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.ptss.support.core.context.IRequestContextService
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.common.exceptions.APIException

@Path("/api/test")
@Tag(name = "Test Controller", description = "Endpoints for testing error handling and request context")
class TestController @Inject constructor(
    private val requestContextService: IRequestContextService
) {
    @GET
    @Path("/success")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Test successful response")
    @RateLimit(value = 2, window = 10) // 2 requests per 10 seconds
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
    )
    fun testSuccess(): Map<String, String> {
        return mapOf(
            "message" to "Success!",
            "requestId" to requestContextService.getCurrentRequestId(),
            "path" to requestContextService.getCurrentPath()
        )
    }

    @GET
    @Path("/error")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Test error handling")
    @APIResponses(
        APIResponse(responseCode = "401", description = "Authentication error")
    )
    fun testError(): Nothing {
        throw APIException(
            message = "Test authentication error",
            errorCode = ErrorCode.INVALID_TOKEN
        )
    }

    @GET
    @Path("/forbidden")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Test forbidden access")
    @APIResponses(
        APIResponse(responseCode = "403", description = "Forbidden access")
    )
    fun testForbidden(): Nothing {
        throw ForbiddenException("Test forbidden access")
    }

    @GET
    @Path("/server-error")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Test internal server error")
    @APIResponses(
        APIResponse(responseCode = "500", description = "Internal server error")
    )
    fun testServerError(): Nothing {
        throw RuntimeException("Test server error")
    }
}