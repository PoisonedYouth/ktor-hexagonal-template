package com.poisonedyouth.ktorhexagonaltemplate.bootstrap

import com.poisonedyouth.ktorhexagonaltemplate.test.WithDatabase
import com.poisonedyouth.ktorhexagonaltemplate.test.executeApplicationTest
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@WithDatabase
class KtorHexagonalTemplateApplicationTest {

    @Test
    fun `verify that application is starting successful`() = executeApplicationTest {

        // Check that application can start successful
        val response = client.get("health")
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(response.bodyAsText()).isEqualToIgnoringWhitespace("{\"status\":\"UP\"}")
    }
}
