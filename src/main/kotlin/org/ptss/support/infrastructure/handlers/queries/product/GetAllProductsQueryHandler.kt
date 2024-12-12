package org.ptss.support.infrastructure.handlers.queries.product

import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.queries.GetAllProductsQuery
import org.ptss.support.infrastructure.repositories.ProductRepository
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.models.Product
import org.slf4j.LoggerFactory

@ApplicationScoped
class GetAllProductsQueryHandler(
    private val productRepository: ProductRepository
) : IQueryHandler<GetAllProductsQuery, List<Product>> {
    private val logger = LoggerFactory.getLogger(GetAllProductsQueryHandler::class.java)

    override suspend fun handleAsync(query: GetAllProductsQuery): List<Product> {
        return logger.executeWithExceptionLoggingAsync(
            operation = {
                productRepository.getAll()
            },
            logMessage = "Error retrieving all products"
        )
    }
}

