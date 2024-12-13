package org.ptss.support.domain.commands

data class CreateProductCommand(
    val name: String,
    val description: String,
    val media: List<String>,
)
