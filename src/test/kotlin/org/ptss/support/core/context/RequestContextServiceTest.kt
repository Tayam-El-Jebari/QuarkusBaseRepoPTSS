package org.ptss.support.core.context

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import jakarta.inject.Inject

@QuarkusTest
class RequestContextServiceTest {

    @Inject
    lateinit var requestContextService: RequestContextService

    @BeforeEach
    fun setup() {
        requestContextService.clearContext()
    }

    @Test
    fun `test context management`() {
        val requestId = "test-request-id"
        val path = "/test/path"

        requestContextService.setContext(requestId, path)

        assertEquals(requestId, requestContextService.getCurrentRequestId())
        assertEquals(path, requestContextService.getCurrentPath())

        requestContextService.clearContext()

        assertNotEquals(requestId, requestContextService.getCurrentRequestId())
        assertEquals("", requestContextService.getCurrentPath())
    }
}