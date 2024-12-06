package cqrs.infrastructure.handlers.queries

import cqrs.core.dtos.ProductDto
import cqrs.core.interfaces.IQueryHandler
import cqrs.core.queries.GetAllProductsQuery
import cqrs.core.queries.GetProductByIdQuery
import cqrs.infrastructure.repositories.ProductRepository
import jakarta.enterprise.context.ApplicationScoped
import org.slf4j.LoggerFactory

@ApplicationScoped
class GetProductByIdQueryHandler(
    private val productRepository: ProductRepository
) : IQueryHandler<GetProductByIdQuery, ProductDto?> {
    private val logger = LoggerFactory.getLogger(GetProductByIdQueryHandler::class.java)

    override suspend fun handleAsync(query: GetProductByIdQuery): ProductDto? {
        return try {
            productRepository.getById(query.id)?.let { product ->
                ProductDto(
                    name = product.name,
                    description = product.description,
                    media = product.media
                )
            }
        } catch (ex: Exception) {
            logger.error("Error retrieving product ${query.id}", ex)
            throw RuntimeException("Error retrieving product", ex)
        }
    }
}


@ApplicationScoped
class GetAllProductsQueryHandler(
    private val productRepository: ProductRepository
) : IQueryHandler<GetAllProductsQuery, List<ProductDto>> {
    private val logger = LoggerFactory.getLogger(GetAllProductsQueryHandler::class.java)

    override suspend fun handleAsync(query: GetAllProductsQuery): List<ProductDto> {
        return try {
            productRepository.getAll().map { product ->
                ProductDto(
                    name = product.name,
                    description = product.description,
                    media = product.media
                )
            }
        } catch (ex: Exception) {
            logger.error("Error retrieving products", ex)
            throw RuntimeException("Error retrieving products", ex)
        }
    }
}
