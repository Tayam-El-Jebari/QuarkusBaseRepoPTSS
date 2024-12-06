package cqrs.core.models

import java.math.BigDecimal
import java.util.*

data class Product(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val media: List<String> = emptyList()
)
