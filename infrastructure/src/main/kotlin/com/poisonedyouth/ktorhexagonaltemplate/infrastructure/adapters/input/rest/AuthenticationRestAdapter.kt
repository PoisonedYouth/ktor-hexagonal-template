package com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.input.rest

import com.poisonedyouth.ktorhexagonaltemplate.application.user.usecases.WriteUserUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.registerAuthenticationRoutes() {
    val writeUserUseCase by inject<WriteUserUseCase>()
    routing {
        route("/api/v1/authentication") {
            post("user") {
                val user = call.receive<NewUserDto>()
                val response = writeUserUseCase.add(user.toUser())
                call.respond(HttpStatusCode.Created, response)
            }
        }
    }
}
