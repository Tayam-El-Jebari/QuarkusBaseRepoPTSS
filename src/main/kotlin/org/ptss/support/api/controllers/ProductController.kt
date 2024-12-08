package org.ptss.support.api.controllers

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.core.MediaType
import org.ptss.support.api.dtos.requests.CreateProductRequest
import org.ptss.support.api.dtos.responses.ProductResponse
import org.ptss.support.core.context.RequestContext
import org.ptss.support.domain.commands.CreateProductCommand
import org.ptss.support.core.services.ProductService

@Path("/products")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class ProductController(
    private val productService: ProductService,
    private val requestContext: RequestContext
) {
    @GET
    suspend fun getAllProducts(): List<ProductResponse> =
        productService.getAllProductsAsync().map { product ->
            ProductResponse(
                id = product.id,
                name = product.name,
                description = product.description,
                media = product.media
            )
        }

    @GET
    @Path("/{id}")
    suspend fun getProductById(@PathParam("id") id: String): ProductResponse? =
        productService.getProductByIdAsync(id)?.let { product ->
            ProductResponse(
                id = product.id,
                name = product.name,
                description = product.description,
                media = product.media
            )
        }

    @POST
    suspend fun createProduct(request: CreateProductRequest): String =
        productService.createProductAsync(
            CreateProductCommand(
                name = request.name,
                description = request.description,
                media = request.media
            )
        )
}
