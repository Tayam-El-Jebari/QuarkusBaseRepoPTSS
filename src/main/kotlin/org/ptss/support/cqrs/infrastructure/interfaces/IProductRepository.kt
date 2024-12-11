package org.ptss.support.cqrs.infrastructure.interfaces

import org.ptss.support.cqrs.core.models.Product

interface IProductRepository {
    fun create(product: Product): String
    fun getById(id: String): Product?
    fun getAll(): List<Product>
}
