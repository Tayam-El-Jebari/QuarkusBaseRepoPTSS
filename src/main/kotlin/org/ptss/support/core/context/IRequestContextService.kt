package org.ptss.support.core.context

interface IRequestContextService {
    fun getCurrentRequestId(): String
    fun getCurrentPath(): String
    fun setContext(requestId: String, path: String)
    fun clearContext()
}