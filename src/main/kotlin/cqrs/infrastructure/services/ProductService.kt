package cqrs.infrastructure.services

import cqrs.core.commands.CreateProductCommand
import cqrs.core.dtos.ProductDto
import cqrs.core.interfaces.ICommandHandler
import cqrs.core.interfaces.IQueryHandler
import cqrs.core.queries.GetAllProductsQuery
import cqrs.core.queries.GetProductByIdQuery
import cqrs.infrastructure.util.executeWithExceptionLoggingAsync
import jakarta.enterprise.context.ApplicationScoped
import org.slf4j.Logger

@ApplicationScoped
class ProductService(
    private val getProductByIdHandler: IQueryHandler<GetProductByIdQuery, ProductDto?>,
    private val getAllProductsHandler: IQueryHandler<GetAllProductsQuery, List<ProductDto>>,
    private val createProductHandler: ICommandHandler<CreateProductCommand, String>,
    private val logger: Logger
) {
    suspend fun getProductByIdAsync(productId: String): ProductDto? {
        return logger.executeWithExceptionLoggingAsync(
            operation = { getProductByIdHandler.handleAsync(GetProductByIdQuery(productId)) },
            errorMessage = "Error retrieving product $productId"
        )
    }

    suspend fun getAllProductsAsync(): List<ProductDto> {
        return logger.executeWithExceptionLoggingAsync(
            operation = { getAllProductsHandler.handleAsync(GetAllProductsQuery()) },
            errorMessage = "Error retrieving all products"
        )
    }

    suspend fun createProductAsync(command: CreateProductCommand): String {
        return logger.executeWithExceptionLoggingAsync(
            operation = { createProductHandler.handleAsync(command) },
            errorMessage = "Error creating product ${command.name}"
        )
    }
}