package org.ptss.support.infrastructure.util

import org.slf4j.Logger

suspend fun <T> Logger.executeWithExceptionLoggingAsync(
    operation: suspend () -> T,
    logMessage: String,
    exceptionHandling: ((Exception) -> Exception)? = null,
    vararg args: Any,
): T {
    return try {
        operation()
    } catch (ex: Exception) {
        this.error(logMessage.format(*args), ex)
        throw exceptionHandling?.invoke(ex) ?: ex
    }
}
