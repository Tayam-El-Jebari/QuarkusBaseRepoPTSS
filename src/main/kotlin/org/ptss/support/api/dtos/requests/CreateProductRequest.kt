package org.ptss.support.api.dtos.requests

data class CreateProductRequest(
    val name: String,
    val description: String,
    val media: List<String>
)