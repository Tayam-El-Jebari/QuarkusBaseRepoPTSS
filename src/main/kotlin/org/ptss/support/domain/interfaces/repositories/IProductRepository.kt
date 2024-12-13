package org.ptss.support.domain.interfaces.repositories

import org.ptss.support.domain.models.Product

interface IProductRepository {
    fun create(product: Product): String
    fun getById(id: String): Product?
    fun getAll(): List<Product>
}
