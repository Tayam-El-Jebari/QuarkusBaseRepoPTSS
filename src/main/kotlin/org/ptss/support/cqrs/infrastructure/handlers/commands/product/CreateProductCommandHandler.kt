package org.ptss.support.cqrs.infrastructure.handlers.commands.product

import org.ptss.support.cqrs.core.commands.product.CreateProductCommand
import org.ptss.support.cqrs.core.interfaces.ICommandHandler
import org.ptss.support.cqrs.core.models.Product
import org.ptss.support.cqrs.infrastructure.repositories.ProductRepository
import org.ptss.support.cqrs.infrastructure.util.executeWithExceptionLoggingAsync
import jakarta.enterprise.context.ApplicationScoped
import org.slf4j.LoggerFactory

@ApplicationScoped
class CreateProductCommandHandler(
    private val productRepository: ProductRepository
) : ICommandHandler<CreateProductCommand, String> {
    private val logger = LoggerFactory.getLogger(CreateProductCommandHandler::class.java)

    override suspend fun handleAsync(command: CreateProductCommand): String {
        return logger.executeWithExceptionLoggingAsync(
            operation = {
                val product = Product(
                    name = command.name,
                    description = command.description,
                    media = command.media
                )
                productRepository.create(product)
            },
            logMessage = "Error creating product with name: ${command.name}"
        )
    }
}


