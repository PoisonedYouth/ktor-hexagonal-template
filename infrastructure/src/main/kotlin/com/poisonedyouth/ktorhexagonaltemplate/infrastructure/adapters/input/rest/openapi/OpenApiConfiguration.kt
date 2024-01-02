package com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.input.rest.openapi

import io.ktor.server.application.Application
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.routing

fun Application.registerOpenApiSpecificationEndpoint() {
    routing { swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml") }
}
