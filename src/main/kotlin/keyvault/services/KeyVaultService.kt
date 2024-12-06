package keyvault.services

import jakarta.enterprise.context.ApplicationScoped
import keyvault.util.executeWithExceptionLoggingAsync
import org.eclipse.microprofile.config.ConfigProvider
import org.slf4j.LoggerFactory

@ApplicationScoped
class KeyVaultService {
    private val logger = LoggerFactory.getLogger(this::class.java)

    // Generic secret retrieval method
    suspend fun getSecret(secretName: String): String? {
        return logger.executeWithExceptionLoggingAsync(
            operation = {
                val config = ConfigProvider.getConfig()
                config.getOptionalValue("secrets.$secretName", String::class.java).orElse(null)
            },
            logMessage = "Error retrieving secret: %s",
            args = *arrayOf(secretName)
        )
    }

    // Method to list available secret names
    fun listSecretNames(): List<String> {
        val config = ConfigProvider.getConfig()
        return config.propertyNames
            .filter { it.startsWith("secrets.") }
            .map { it.removePrefix("secrets.") }
    }
}