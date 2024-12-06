package cqrs.core.commands

import java.math.BigDecimal

data class CreateProductCommand(
    val name: String,
    val description: String,
    val media: List<String>
)

