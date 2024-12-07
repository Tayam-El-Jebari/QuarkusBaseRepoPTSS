package org.ptss.support.cqrs.core.commands.product

data class CreateProductCommand(
    val name: String,
    val description: String,
    val media: List<String>
)

