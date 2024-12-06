package cqrs.infrastructure.handlers.queries.product

import cqrs.core.dtos.ProductDto
import cqrs.core.interfaces.IQueryHandler
import cqrs.core.queries.product.GetAllProductsQuery
import cqrs.infrastructure.repositories.ProductRepository
import cqrs.infrastructure.util.executeWithExceptionLoggingAsync
import jakarta.enterprise.context.ApplicationScoped
import org.slf4j.LoggerFactory


@ApplicationScoped
class GetAllProductsQueryHandler(
    private val productRepository: ProductRepository
) : IQueryHandler<GetAllProductsQuery, List<ProductDto>> {
    private val logger = LoggerFactory.getLogger(GetAllProductsQueryHandler::class.java)

    override suspend fun handleAsync(query: GetAllProductsQuery): List<ProductDto> {
        return logger.executeWithExceptionLoggingAsync(
            operation = {
                productRepository.getAll().map { product ->
                    ProductDto(
                        name = product.name,
                        description = product.description,
                        media = product.media
                    )
                }
            },
            logMessage = "Error retrieving all products"
        )
    }
}

