package org.ptss.support.cqrs.core.dtos

data class ProductDto(
    val name: String,
    val description: String,
    val media: List<String>
)

