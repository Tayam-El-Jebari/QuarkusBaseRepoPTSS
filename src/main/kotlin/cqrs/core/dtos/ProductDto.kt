package cqrs.core.dtos

import java.math.BigDecimal

data class ProductDto(
    val name: String,
    val price: BigDecimal
)
