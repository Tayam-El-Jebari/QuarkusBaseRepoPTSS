package cqrs.infrastructure.interfaces

import cqrs.core.models.Product

interface IProductRepository {
    fun create(product: Product): String
    fun getById(id: String): Product?
    fun getAll(): List<Product>
}