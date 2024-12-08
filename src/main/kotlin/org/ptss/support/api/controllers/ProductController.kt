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
import org.ptss.support.api.dtos.responses.CreateProductResponse
import org.ptss.support.api.dtos.responses.ProductResponse
import org.ptss.support.core.facades.ProductFacade

@Path("/products")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class ProductController(
    private val productFacade: ProductFacade
) {
    @GET
    suspend fun getAllProducts(): List<ProductResponse> =
        productFacade.getAllProducts()

    @GET
    @Path("/{id}")
    suspend fun getProductById(@PathParam("id") id: String): ProductResponse? =
        productFacade.getProductById(id)

    @POST
    suspend fun createProduct(request: CreateProductRequest): CreateProductResponse =
        CreateProductResponse(productFacade.createProduct(request))
}
