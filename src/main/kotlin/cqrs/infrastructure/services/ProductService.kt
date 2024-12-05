package cqrs.infrastructure.services

import cqrs.core.commands.CreateProductCommand
import cqrs.core.dtos.ProductDto
import cqrs.core.interfaces.ICommandHandler
import cqrs.core.interfaces.IQueryHandler
import cqrs.core.queries.GetAllProductsQuery
import cqrs.core.queries.GetProductByIdQuery
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
        return try {
            getProductByIdHandler.handleAsync(GetProductByIdQuery(productId))
        } catch (ex: Exception) {
            logger.error("Error retrieving product $productId", ex)
            throw ex
        }
    }

    suspend fun getAllProductsAsync(): List<ProductDto> {
        return try {
            getAllProductsHandler.handleAsync(GetAllProductsQuery())
        } catch (ex: Exception) {
            logger.error("Error retrieving all products", ex)
            throw ex
        }
    }

    suspend fun createProductAsync(command: CreateProductCommand): String {
        return try {
            createProductHandler.handleAsync(command)
        } catch (ex: Exception) {
            logger.error("Error creating product ${command.name}", ex)
            throw ex
        }
    }
}