package org.ptss.support.cqrs.infrastructure.handlers.queries.product

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.cqrs.core.dtos.ProductDto
import org.ptss.support.cqrs.core.interfaces.IQueryHandler
import org.ptss.support.cqrs.core.queries.product.GetProductByIdQuery
import org.ptss.support.cqrs.infrastructure.repositories.ProductRepository
import org.ptss.support.cqrs.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory

@ApplicationScoped
class GetProductByIdQueryHandler(
    private val productRepository: ProductRepository,
) : IQueryHandler<GetProductByIdQuery, ProductDto?> {
    private val logger = LoggerFactory.getLogger(GetProductByIdQueryHandler::class.java)

    override suspend fun handleAsync(query: GetProductByIdQuery): ProductDto? {
        return logger.executeWithExceptionLoggingAsync(
            operation = {
                productRepository.getById(query.id)?.let { product ->
                    ProductDto(
                        name = product.name,
                        description = product.description,
                        media = product.media,
                    )
                }
            },
            logMessage = "Error retrieving product with ID: ${query.id}",
        )
    }
}
