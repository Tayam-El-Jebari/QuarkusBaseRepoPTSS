package org.ptss.support.infrastructure.persistence.entities


import com.azure.data.tables.models.TableEntity
import org.ptss.support.domain.models.Product

data class ProductEntity(
    var name: String = "",
    var description: String = "",
    var media: String = ""
) {
    // Creating a TableEntity instance to handle the actual storage
    fun toTableEntity(product: Product): TableEntity {
        return TableEntity("PRODUCT", product.id).apply {
            properties.apply {
                put("name", name)
                put("description", description)
                put("media", media)
            }
        }
    }

    companion object {
        fun fromTableEntity(entity: TableEntity): ProductEntity {
            return ProductEntity(
                name = entity.properties["name"] as String,
                description = entity.properties["description"] as String,
                media = entity.properties["media"] as String
            )
        }
    }

    fun toDomain(): Product = Product(
        id = "", // This should come from the TableEntity
        name = name,
        description = description,
        media = media.split(",").filter { it.isNotEmpty() }
    )
}