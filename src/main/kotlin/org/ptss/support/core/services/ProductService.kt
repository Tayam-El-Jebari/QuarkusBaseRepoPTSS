package org.ptss.support.core.services

import org.ptss.support.domain.commands.CreateProductCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.queries.GetAllProductsQuery
import org.ptss.support.domain.queries.GetProductByIdQuery
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.enums.ErrorCode
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
            operation = {
                getProductByIdHandler.handleAsync(GetProductByIdQuery(productId))
                    ?: throw APIException(
                        errorCode = ErrorCode.PRODUCT_NOT_FOUND,
                        message = "Product with ID $productId not found"
                    )
            },
            logMessage = "Error retrieving product $productId",
            exceptionHandling = { ex ->
                when (ex) {
                    is APIException -> ex
                    else -> APIException(
                        errorCode = ErrorCode.PRODUCT_CREATION_ERROR,
                        message = "Unable to retrieve product with ID: $productId",
                    )
                }
            }
        )
    }

    suspend fun getAllProductsAsync(): List<Product> {
        return logger.executeWithExceptionLoggingAsync(
            operation = { getAllProductsHandler.handleAsync(GetAllProductsQuery()) },
            logMessage = "Error retrieving all products",
            exceptionHandling = { ex ->
                APIException(
                    errorCode = ErrorCode.PRODUCT_CREATION_ERROR,
                    message = "Unable to retrieve products",
                )
            }
        )
    }

    suspend fun createProductAsync(command: CreateProductCommand): String {
        validateProductCommand(command)
        return logger.executeWithExceptionLoggingAsync(
            operation = { createProductHandler.handleAsync(command) },
            logMessage = "Error creating product ${command.name}",
            exceptionHandling = { ex ->
                APIException(
                    errorCode = ErrorCode.PRODUCT_CREATION_ERROR,
                    message = "Failed to create product ${command.name}",
                )
            }
        )
    }

    private fun validateProductCommand(command: CreateProductCommand) {
        val validationErrors = mutableListOf<String>()

        when {
            command.name.isBlank() -> validationErrors.add("Product name cannot be empty")
            command.name.length > 100 -> validationErrors.add("Product name cannot exceed 100 characters")
            command.description.length > 1000 -> validationErrors.add("Product description cannot exceed 1000 characters")
            command.media.size > 10 -> validationErrors.add("Product cannot have more than 10 media items")
            command.media.any { it.length > 500 } -> validationErrors.add("Media URLs cannot exceed 500 characters")
        }

        if (validationErrors.isNotEmpty()) {
            throw APIException(
                errorCode = ErrorCode.PRODUCT_VALIDATION_ERROR,
                message = validationErrors.joinToString("; ")
            )
        }
    }
}
