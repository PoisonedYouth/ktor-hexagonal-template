package com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.input.rest

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.registerHealthRoute() {
    routing { route("health") { get { call.respond(HttpStatusCode.OK, mapOf("status" to "UP")) } } }
}
