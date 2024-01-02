package com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.input.rest

import com.poisonedyouth.ktorhexagonaltemplate.common.exception.NotFoundException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

fun Application.installExceptionHandling() {
    install(StatusPages) {
        exception<IllegalArgumentException> { call, e ->
            call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
        }
        exception<NotFoundException> { call, e ->
            call.respond(HttpStatusCode.NotFound, e.localizedMessage)
        }
    }
}
