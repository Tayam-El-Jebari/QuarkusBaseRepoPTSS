package org.ptss.support.infrastructure.handlers.queries.product

import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.queries.GetProductByIdQuery
import org.ptss.support.infrastructure.repositories.ProductRepository
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.models.Product
import org.slf4j.LoggerFactory

@ApplicationScoped
class GetProductByIdQueryHandler(
    private val productRepository: ProductRepository
) : IQueryHandler<GetProductByIdQuery, Product?> {
    private val logger = LoggerFactory.getLogger(GetProductByIdQueryHandler::class.java)

    override suspend fun handleAsync(query: GetProductByIdQuery): Product? {
        return logger.executeWithExceptionLoggingAsync(
            operation = {
                productRepository.getById(query.id)
            },
            logMessage = "Error retrieving product with ID: ${query.id}"
        )
    }
}
