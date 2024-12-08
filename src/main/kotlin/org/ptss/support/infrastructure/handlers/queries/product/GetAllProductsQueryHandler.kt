package org.ptss.support.infrastructure.handlers.queries.product

import org.ptss.support.api.dtos.ProductDto
import org.ptss.support.domain.interfaces.cqrs.IQueryHandler
import org.ptss.support.domain.querries.GetAllProductsQuery
import org.ptss.support.infrastructure.repositories.ProductRepository
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
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

