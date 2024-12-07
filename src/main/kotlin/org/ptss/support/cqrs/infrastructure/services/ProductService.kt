package org.ptss.support.cqrs.infrastructure.services

import org.ptss.support.cqrs.core.commands.product.CreateProductCommand
import org.ptss.support.cqrs.core.dtos.ProductDto
import org.ptss.support.cqrs.core.interfaces.ICommandHandler
import org.ptss.support.cqrs.core.interfaces.IQueryHandler
import org.ptss.support.cqrs.core.queries.product.GetAllProductsQuery
import org.ptss.support.cqrs.core.queries.product.GetProductByIdQuery
import org.ptss.support.cqrs.infrastructure.util.executeWithExceptionLoggingAsync
import jakarta.enterprise.context.ApplicationScoped
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ApplicationScoped
class ProductService(
    private val getProductByIdHandler: IQueryHandler<GetProductByIdQuery, ProductDto?>,
    private val getAllProductsHandler: IQueryHandler<GetAllProductsQuery, List<ProductDto>>,
    private val createProductHandler: ICommandHandler<CreateProductCommand, String>,
    private val logger: Logger = LoggerFactory.getLogger(ProductService::class.java)
) {
    suspend fun getProductByIdAsync(productId: String): ProductDto? {
        return logger.executeWithExceptionLoggingAsync(
            operation = { getProductByIdHandler.handleAsync(GetProductByIdQuery(productId)) },
            logMessage = "Error retrieving product $productId",
            exceptionHandling = { ex ->
                RuntimeException("Custom message: Unable to retrieve product with ID: $productId", ex)
            }
        )
    }

    suspend fun getAllProductsAsync(): List<ProductDto> {
        return logger.executeWithExceptionLoggingAsync(
            operation = { getAllProductsHandler.handleAsync(GetAllProductsQuery()) },
            logMessage = "Error retrieving all products",
            exceptionHandling = { ex ->
                RuntimeException("Custom message: Unable to retrieve products", ex)
            }
        )
    }

    suspend fun createProductAsync(command: CreateProductCommand): String {
        return logger.executeWithExceptionLoggingAsync(
            operation = { createProductHandler.handleAsync(command) },
            logMessage = "Error creating product ${command.name}",
            exceptionHandling = { ex ->
                IllegalStateException("Custom message: Failed to create product ${command.name}", ex)
            }
        )
    }
}
