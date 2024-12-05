package cqrs.core.queries

data class GetProductByIdQuery(val id: String)
data class GetAllProductsQuery(val placeholder: Boolean = true)

