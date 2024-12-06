package keyvault.resource

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import keyvault.services.KeyVaultService

@Path("/key-vault")
class KeyVaultResource(
    private val keyVaultService: KeyVaultService
) {
    @GET
    @Path("/{secretName}")
    @Produces(MediaType.TEXT_PLAIN)
    suspend fun getSecret(@PathParam("secretName") secretName: String): String? {
        return keyVaultService.getSecret(secretName)
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    fun listSecrets(): List<String> {
        return keyVaultService.listSecretNames()
    }
}