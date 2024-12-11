package org.ptss.support.cqrs.infrastructure.handlers.queries.product

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.cqrs.core.dtos.ProductDto
import org.ptss.support.cqrs.core.interfaces.IQueryHandler
import org.ptss.support.cqrs.core.queries.product.GetAllProductsQuery
import org.ptss.support.cqrs.infrastructure.repositories.ProductRepository
import org.ptss.support.cqrs.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory

@ApplicationScoped
class GetAllProductsQueryHandler(
    private val productRepository: ProductRepository,
) : IQueryHandler<GetAllProductsQuery, List<ProductDto>> {
    private val logger = LoggerFactory.getLogger(GetAllProductsQueryHandler::class.java)

    override suspend fun handleAsync(query: GetAllProductsQuery): List<ProductDto> {
        return logger.executeWithExceptionLoggingAsync(
            operation = {
                productRepository.getAll().map { product ->
                    ProductDto(
                        name = product.name,
                        description = product.description,
                        media = product.media,
                    )
                }
            },
            logMessage = "Error retrieving all products",
        )
    }
}
