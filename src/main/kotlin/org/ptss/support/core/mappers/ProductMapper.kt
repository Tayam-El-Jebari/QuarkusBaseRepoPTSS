package org.ptss.support.core.mappers

import org.ptss.support.api.dtos.requests.CreateProductRequest
import org.ptss.support.api.dtos.responses.ProductResponse
import org.ptss.support.domain.commands.CreateProductCommand
import org.ptss.support.domain.models.Product

object ProductMapper {
    fun toCommand(request: CreateProductRequest) = CreateProductCommand(
        name = request.name,
        description = request.description,
        media = request.media
    )

    fun toResponse(product: Product) = ProductResponse(
        id = product.id,
        name = product.name,
        description = product.description,
        media = product.media
    )
}
