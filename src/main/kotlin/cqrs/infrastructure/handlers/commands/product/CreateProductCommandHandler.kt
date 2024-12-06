package cqrs.infrastructure.handlers.commands.product

import cqrs.core.commands.product.CreateProductCommand
import cqrs.core.interfaces.ICommandHandler
import cqrs.core.models.Product
import cqrs.infrastructure.repositories.ProductRepository
import cqrs.infrastructure.util.executeWithExceptionLoggingAsync
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


