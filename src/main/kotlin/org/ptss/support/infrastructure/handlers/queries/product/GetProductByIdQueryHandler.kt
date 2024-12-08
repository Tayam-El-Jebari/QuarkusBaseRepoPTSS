package org.ptss.support.infrastructure.handlers.queries.product

import org.ptss.support.api.dtos.ProductDto
import org.ptss.support.domain.interfaces.cqrs.IQueryHandler
import org.ptss.support.domain.querries.GetProductByIdQuery
import org.ptss.support.infrastructure.repositories.ProductRepository
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import jakarta.enterprise.context.ApplicationScoped
import org.slf4j.LoggerFactory

@ApplicationScoped
class GetProductByIdQueryHandler(
    private val productRepository: ProductRepository
) : IQueryHandler<GetProductByIdQuery, ProductDto?> {
    private val logger = LoggerFactory.getLogger(GetProductByIdQueryHandler::class.java)

    override suspend fun handleAsync(query: GetProductByIdQuery): ProductDto? {
        return logger.executeWithExceptionLoggingAsync(
            operation = {
                productRepository.getById(query.id)?.let { product ->
                    ProductDto(
                        name = product.name,
                        description = product.description,
                        media = product.media
                    )
                }
            },
            logMessage = "Error retrieving product with ID: ${query.id}"
        )
    }
}

