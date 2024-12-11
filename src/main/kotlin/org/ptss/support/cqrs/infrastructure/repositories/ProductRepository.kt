package org.ptss.support.cqrs.infrastructure.repositories

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.cqrs.core.models.Product
import org.ptss.support.cqrs.infrastructure.interfaces.IProductRepository

@ApplicationScoped
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
