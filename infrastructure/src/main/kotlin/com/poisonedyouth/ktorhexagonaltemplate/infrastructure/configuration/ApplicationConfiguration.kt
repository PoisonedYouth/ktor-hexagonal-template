package com.poisonedyouth.ktorhexagonaltemplate.infrastructure.configuration

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.input.rest.installExceptionHandling
import com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.input.rest.openapi.registerOpenApiSpecificationEndpoint
import com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.input.rest.registerAuthenticationRoutes
import com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.input.rest.registerHealthRoute
import com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.input.rest.registerUserRoutes
import com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.input.rest.security.installBasicAuthentication
import com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.output.exposed.flyway.configureAndMigrateDatabase
import com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.output.exposed.initializeDatabaseConnection
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun Application.main() {
    installKoin()
    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
            registerModule(JavaTimeModule())
        }
    }
    installExceptionHandling()
    installBasicAuthentication()
    initializeDatabaseConnection()
    configureAndMigrateDatabase()
    registerAuthenticationRoutes()
    registerUserRoutes()
    registerHealthRoute()
    registerOpenApiSpecificationEndpoint()
}
