package cqrs.core.commands

data class CreateProductCommand(
    val name: String,
    val description: String,
    val media: List<String>
)

