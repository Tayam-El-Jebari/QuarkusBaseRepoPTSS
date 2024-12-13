package org.ptss.support.domain.models

import java.util.UUID

data class Product(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val media: List<String> = emptyList(),
)
