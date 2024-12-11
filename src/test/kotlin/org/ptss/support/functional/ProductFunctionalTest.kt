package org.ptss.support.functional

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.ptss.support.api.dtos.requests.CreateProductRequest
import org.ptss.support.core.facades.ProductFacade
import org.ptss.support.core.services.ProductService
import org.ptss.support.domain.commands.CreateProductCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.models.Product
import org.ptss.support.domain.queries.GetProductByIdQuery
import org.ptss.support.infrastructure.handlers.queries.product.GetAllProductsQueryHandler

class ProductFunctionalTest {
    private lateinit var productFacade: ProductFacade
    private lateinit var productService: ProductService
    private lateinit var getProductByIdHandler: IQueryHandler<GetProductByIdQuery, Product?>
    private lateinit var getAllProductsHandler: GetAllProductsQueryHandler
    private lateinit var createProductHandler: ICommandHandler<CreateProductCommand, String>

    @BeforeEach
    fun setUp() {
        // Create mock dependencies
        getProductByIdHandler = mockk()
        getAllProductsHandler = mockk()
        createProductHandler = mockk()

        // Initialize the service with mocked dependencies
        productService = ProductService(
            getProductByIdHandler,
            getAllProductsHandler,
            createProductHandler
        )

        // Initialize the facade with the service
        productFacade = ProductFacade(productService)
    }

    @Test
    fun `create and retrieve product`() = runTest {
        // Arrange
        val createRequest = CreateProductRequest(
            name = "New Product",
            description = "A test product",
            media = listOf("image1.jpg")
        )
        val expectedProductId = "generated-product-id"
        val expectedProduct = Product(
            id = expectedProductId,
            name = createRequest.name,
            description = createRequest.description,
            media = createRequest.media
        )

        coEvery { createProductHandler.handleAsync(any()) } returns expectedProductId
        coEvery { getProductByIdHandler.handleAsync(GetProductByIdQuery(expectedProductId)) } returns expectedProduct

        // Act
        val createResponse = productFacade.createProduct(createRequest)
        val retrievedProduct = productFacade.getProductById(createResponse)

        // Assert
        assertEquals(expectedProductId, createResponse)
        assertNotNull(retrievedProduct)
        assertEquals(expectedProductId, retrievedProduct?.id)
        assertEquals(createRequest.name, retrievedProduct?.name)
        assertEquals(createRequest.description, retrievedProduct?.description)
        assertEquals(createRequest.media, retrievedProduct?.media)

        coVerify { createProductHandler.handleAsync(any()) }
        coVerify { getProductByIdHandler.handleAsync(GetProductByIdQuery(expectedProductId)) }
    }

    @Test
    fun `retrieve all products`() = runTest {
        // Arrange
        val expectedProducts = listOf(
            Product(id = "1", name = "Product 1", description = "Description 1", media = listOf("image1.jpg")),
            Product(id = "2", name = "Product 2", description = "Description 2", media = listOf("image2.jpg"))
        )

        // Use any() instead of an exact query instance
        coEvery { getAllProductsHandler.handleAsync(any()) } returns expectedProducts

        // Act
        val products = productFacade.getAllProducts()

        // Assert
        assertEquals(expectedProducts.size, products.size)
        assertEquals(expectedProducts[0].id, products[0].id)
        assertEquals(expectedProducts[1].id, products[1].id)

        coVerify { getAllProductsHandler.handleAsync(any()) }
    }
}