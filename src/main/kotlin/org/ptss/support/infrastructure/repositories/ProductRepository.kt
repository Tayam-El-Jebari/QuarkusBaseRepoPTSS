package org.ptss.support.infrastructure.repositories

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.models.Product
import org.ptss.support.domain.interfaces.repositories.IProductRepository

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