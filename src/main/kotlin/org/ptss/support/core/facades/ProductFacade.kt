package org.ptss.support.core.facades

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.api.dtos.requests.CreateProductRequest
import org.ptss.support.api.dtos.responses.ProductResponse
import org.ptss.support.core.services.ProductService
import org.ptss.support.core.mappers.ProductMapper

@ApplicationScoped
class ProductFacade @Inject constructor(
    private val productService: ProductService
) {
    suspend fun getAllProducts(): List<ProductResponse> =
        productService.getAllProductsAsync()
            .map(ProductMapper::toResponse)

    suspend fun getProductById(id: String): ProductResponse? =
        productService.getProductByIdAsync(id)
            ?.let(ProductMapper::toResponse)

    suspend fun createProduct(request: CreateProductRequest): String =
        productService.createProductAsync(ProductMapper.toCommand(request))
}
