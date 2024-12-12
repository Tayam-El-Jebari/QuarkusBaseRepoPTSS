package org.ptss.support.api.dtos.responses

data class ProductResponse(
    val id: String,
    val name: String,
    val description: String,
    val media: List<String>
)