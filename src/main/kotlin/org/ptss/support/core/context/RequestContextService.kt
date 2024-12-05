package org.ptss.support.core.context

import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.enterprise.context.ApplicationScoped
import org.slf4j.MDC
import java.util.UUID

@ApplicationScoped
@RegisterForReflection
class RequestContextService  : IRequestContextService {
    private val requestContext = ThreadLocal<RequestContext>()

    override fun getCurrentRequestId(): String {
        return requestContext.get()?.requestId ?: generateRequestId()
    }

    override fun getCurrentPath(): String {
        return requestContext.get()?.path ?: ""
    }

    override fun setContext(requestId: String, path: String) {
        requestContext.set(RequestContext(requestId, path))
        MDC.put("requestId", requestId)
    }

    override fun clearContext() {
        requestContext.remove()
        MDC.clear()
    }

    private fun generateRequestId(): String {
        return UUID.randomUUID().toString()
    }
}