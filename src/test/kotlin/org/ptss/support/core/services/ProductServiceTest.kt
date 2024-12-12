package org.ptss.support.core.services

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.commands.CreateProductCommand
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.models.Product
import org.ptss.support.domain.queries.GetProductByIdQuery
import org.ptss.support.infrastructure.handlers.queries.product.GetAllProductsQueryHandler

class ProductServiceTest {
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
    }

    @Test
    fun `getProductByIdAsync returns product when found`() = runTest {
        // Arrange
        val productId = "test-product-id"
        val expectedProduct = Product(id = productId, name = "Test Product", description = "A test product")

        coEvery { getProductByIdHandler.handleAsync(GetProductByIdQuery(productId)) } returns expectedProduct

        // Act
        val result = productService.getProductByIdAsync(productId)

        // Assert
        assertNotNull(result)
        assertEquals(expectedProduct, result)
        coVerify { getProductByIdHandler.handleAsync(GetProductByIdQuery(productId)) }
    }

    @Test
    fun `getProductByIdAsync throws APIException when product not found`() = runTest {
        // Arrange
        val productId = "non-existent-id"

        coEvery { getProductByIdHandler.handleAsync(GetProductByIdQuery(productId)) } returns null

        // Act & Assert
        val exception = assertThrows<APIException> {
            productService.getProductByIdAsync(productId)
        }

        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.errorCode)
        assertTrue(exception.message.contains(productId))
    }

    @Test
    fun `getAllProductsAsync returns list of products`() = runTest {
        // Arrange
        val expectedProducts = listOf(
            Product(id = "1", name = "Product 1", description = "Description 1"),
            Product(id = "2", name = "Product 2", description = "Description 2")
        )

        coEvery { getAllProductsHandler.handleAsync(any()) } returns expectedProducts

        // Act
        val result = productService.getAllProductsAsync()

        // Assert
        assertEquals(expectedProducts, result)
        coVerify { getAllProductsHandler.handleAsync(any()) }
    }

    @Test
    fun `createProductAsync successfully creates product`() = runTest {
        // Arrange
        val command = CreateProductCommand(
            name = "New Product",
            description = "A test product",
            media = listOf("image1.jpg")
        )
        val expectedProductId = "generated-product-id"

        coEvery { createProductHandler.handleAsync(command) } returns expectedProductId

        // Act
        val result = productService.createProductAsync(command)

        // Assert
        assertEquals(expectedProductId, result)
        coVerify { createProductHandler.handleAsync(command) }
    }

    @Test
    fun `validateProductCommand throws exception for empty product name`() = runTest {
        // Arrange
        val invalidCommand = CreateProductCommand(
            name = "",
            description = "Valid description",
            media = listOf()
        )

        // Act & Assert
        val exception = assertThrows<APIException> {
            productService.createProductAsync(invalidCommand)
        }

        assertEquals(ErrorCode.PRODUCT_VALIDATION_ERROR, exception.errorCode)
        assertTrue(exception.message.contains("Product name cannot be empty"))
    }

    @Test
    fun `validateProductCommand throws exception for long product name`() = runTest {
        // Arrange
        val longName = "a".repeat(101)
        val invalidCommand = CreateProductCommand(
            name = longName,
            description = "Valid description",
            media = listOf()
        )

        // Act & Assert
        val exception = assertThrows<APIException> {
            productService.createProductAsync(invalidCommand)
        }

        assertEquals(ErrorCode.PRODUCT_VALIDATION_ERROR, exception.errorCode)
        assertTrue(exception.message.contains("Product name cannot exceed 100 characters"))
    }

    @Test
    fun `validateProductCommand throws exception for long description`() = runTest {
        // Arrange
        val longDescription = "a".repeat(1001)
        val invalidCommand = CreateProductCommand(
            name = "Valid Name",
            description = longDescription,
            media = listOf()
        )

        // Act & Assert
        val exception = assertThrows<APIException> {
            productService.createProductAsync(invalidCommand)
        }

        assertEquals(ErrorCode.PRODUCT_VALIDATION_ERROR, exception.errorCode)
        assertTrue(exception.message.contains("Product description cannot exceed 1000 characters"))
    }

    @Test
    fun `validateProductCommand throws exception for too many media items`() = runTest {
        // Arrange
        val manyMediaItems = List(11) { "media$it.jpg" }
        val invalidCommand = CreateProductCommand(
            name = "Valid Name",
            description = "Valid Description",
            media = manyMediaItems
        )

        // Act & Assert
        val exception = assertThrows<APIException> {
            productService.createProductAsync(invalidCommand)
        }

        assertEquals(ErrorCode.PRODUCT_VALIDATION_ERROR, exception.errorCode)
        assertTrue(exception.message.contains("Product cannot have more than 10 media items"))
    }

    @Test
    fun `validateProductCommand throws exception for long media URL`() = runTest {
        // Arrange
        val longMediaUrl = "a".repeat(501) + ".jpg"
        val invalidCommand = CreateProductCommand(
            name = "Valid Name",
            description = "Valid Description",
            media = listOf(longMediaUrl)
        )

        // Act & Assert
        val exception = assertThrows<APIException> {
            productService.createProductAsync(invalidCommand)
        }

        assertEquals(ErrorCode.PRODUCT_VALIDATION_ERROR, exception.errorCode)
        assertTrue(exception.message.contains("Media URLs cannot exceed 500 characters"))
    }
}