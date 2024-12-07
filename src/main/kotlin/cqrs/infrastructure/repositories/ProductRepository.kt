package cqrs.infrastructure.repositories

import cqrs.core.models.Product
import cqrs.infrastructure.interfaces.IProductRepository

class ProductRepository : IProductRepository {
    private val products = mutableListOf<Product>()

    override fun create(product: Product): String {
        products.add(product)
        return product.id
    }

    override fun getById(id: String): Product? =
        products.find { it.id == id }

    override fun getAll(): List<Product> = products.toList()
}