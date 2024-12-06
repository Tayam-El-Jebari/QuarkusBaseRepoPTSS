package cqrs.infrastructure.util

import org.slf4j.Logger

suspend fun <T> Logger.executeWithExceptionLoggingAsync(
    operation: suspend () -> T,
    errorMessage: String,
    vararg args: Any
): T {
    return try {
        operation()
    } catch (ex: Exception) {
        this.error(errorMessage.format(*args), ex)
        throw ex
    }
}
