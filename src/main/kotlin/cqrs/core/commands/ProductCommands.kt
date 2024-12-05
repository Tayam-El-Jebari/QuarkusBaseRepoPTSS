package cqrs.core.commands

import java.math.BigDecimal

data class CreateProductCommand(
    val name: String,
    val price: BigDecimal
)
