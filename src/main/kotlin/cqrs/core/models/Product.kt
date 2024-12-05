package cqrs.core.models

import java.math.BigDecimal
import java.util.*

data class Product(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val price: BigDecimal
)
