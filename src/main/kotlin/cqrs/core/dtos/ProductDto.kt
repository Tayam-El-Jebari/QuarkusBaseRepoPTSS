package cqrs.core.dtos

import java.math.BigDecimal

data class ProductDto(
    val name: String,
    val description: String,
    val media: List<String>
)

