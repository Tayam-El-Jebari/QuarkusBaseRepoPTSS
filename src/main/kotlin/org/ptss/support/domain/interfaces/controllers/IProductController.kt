package org.ptss.support.domain.interfaces.controllers

import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.ptss.support.api.dtos.requests.CreateProductRequest
import org.ptss.support.api.dtos.responses.CreateProductResponse
import org.ptss.support.api.dtos.responses.ProductResponse
import org.ptss.support.common.exceptions.ServiceError

@Tag(name = "Products", description = "Product management endpoints")
interface IProductController {
    @GET
    @Operation(summary = "Get all products", description = "Retrieves a list of all products")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "List of products successfully retrieved",
            content = [Content(schema = Schema(implementation = Array<ProductResponse>::class))]
        ),
        APIResponse(
            responseCode = "500",
            description = "Internal server error",
            content = [Content(schema = Schema(implementation = ServiceError::class))]
        )
    )
    suspend fun getAllProducts(): List<ProductResponse>

    @GET
    @Path("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieves a specific product by its ID")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Product successfully retrieved",
            content = [Content(schema = Schema(implementation = ProductResponse::class))]
        ),
        APIResponse(
            responseCode = "404",
            description = "Product not found",
            content = [Content(schema = Schema(implementation = ServiceError::class))]
        )
    )
    suspend fun getProductById(
        @Parameter(description = "ID of the product", required = true)
        @PathParam("id") id: String
    ): ProductResponse?

    @POST
    @Operation(summary = "Create product", description = "Creates a new product")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Product successfully created",
            content = [Content(schema = Schema(implementation = CreateProductResponse::class))]
        ),
        APIResponse(
            responseCode = "400",
            description = "Invalid product data",
            content = [Content(schema = Schema(implementation = ServiceError::class))]
        )
    )
    suspend fun createProduct(
        @Parameter(description = "Product creation data", required = true)
        request: CreateProductRequest
    ): CreateProductResponse
}