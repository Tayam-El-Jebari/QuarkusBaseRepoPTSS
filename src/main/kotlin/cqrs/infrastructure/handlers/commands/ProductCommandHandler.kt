package cqrs.infrastructure.handlers.commands

import cqrs.core.commands.CreateProductCommand
import cqrs.core.interfaces.ICommandHandler
import cqrs.core.models.Product
import cqrs.infrastructure.repositories.ProductRepository
import jakarta.enterprise.context.ApplicationScoped
import org.slf4j.LoggerFactory

@ApplicationScoped
class CreateProductCommandHandler(
    private val productRepository: ProductRepository
) : ICommandHandler<CreateProductCommand, String> {
    private val logger = LoggerFactory.getLogger(CreateProductCommandHandler::class.java)

    override suspend fun handleAsync(command: CreateProductCommand): String {
        return try {
            val product = Product(
                name = command.name,
                description = command.description,
                media = command.media
            )
            productRepository.create(product)
        } catch (ex: Exception) {
            logger.error("Error creating product", ex)
            throw RuntimeException("Error creating product", ex)
        }
    }
}
