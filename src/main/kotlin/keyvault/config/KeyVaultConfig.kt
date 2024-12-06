package keyvault.config

import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.Config
import org.eclipse.microprofile.config.ConfigProvider

@ApplicationScoped
class KeyVaultConfig {
    private val config: Config = ConfigProvider.getConfig()

    fun getVaultUrl(): String =
        config.getValue("keyvault.url", String::class.java)

    fun getVaultToken(): String? =
        config.getOptionalValue("keyvault.token", String::class.java).orElse(null)
}