package org.ptss.support.core.services

import org.ptss.support.domain.commands.CreateProductCommand
import org.ptss.support.domain.interfaces.cqrs.ICommandHandler
import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.querries.GetAllProductsQuery
import org.ptss.support.domain.querries.GetProductByIdQuery
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.models.Product
import org.ptss.support.infrastructure.handlers.queries.product.GetAllProductsQueryHandler
import org.slf4j.LoggerFactory

@ApplicationScoped
class ProductService(
    private val getProductByIdHandler: IQueryHandler<GetProductByIdQuery, Product?>,
    private val getAllProductsHandler: GetAllProductsQueryHandler, //used to be iqueryhandler, but bean error. I don't know why :'( pls help
    private val createProductHandler: ICommandHandler<CreateProductCommand, String>
) {
    private val logger = LoggerFactory.getLogger(ProductService::class.java)
    suspend fun getProductByIdAsync(productId: String): Product? {
        return logger.executeWithExceptionLoggingAsync(
            operation = { getProductByIdHandler.handleAsync(GetProductByIdQuery(productId)) },
            logMessage = "Error retrieving product $productId",
            exceptionHandling = { ex ->
                RuntimeException("Custom message: Unable to retrieve product with ID: $productId", ex)
            }
        )
    }

    suspend fun getAllProductsAsync(): List<Product> {
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
