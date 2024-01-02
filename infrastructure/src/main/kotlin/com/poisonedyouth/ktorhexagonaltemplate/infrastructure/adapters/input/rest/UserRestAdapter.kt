package com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.input.rest

import com.poisonedyouth.ktorhexagonaltemplate.application.user.usecases.ReadUserUseCase
import com.poisonedyouth.ktorhexagonaltemplate.application.user.usecases.WriteUserUseCase
import com.poisonedyouth.ktorhexagonaltemplate.common.vo.toIdentity
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.entity.User
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import java.util.UUID
import org.koin.ktor.ext.inject

fun Application.registerUserRoutes() {
    val readUserUseCase by inject<ReadUserUseCase>()
    val writeUserUseCase by inject<WriteUserUseCase>()

    routing {
        route("api/v1/user") {
            authenticate("auth-basic") {
                get {
                    val id = call.request.queryParameters["id"]
                    val userIdentity = UUID.fromString(id).toIdentity()
                    val user: User? = readUserUseCase.find(userIdentity)
                    if (user != null) {
                        call.respond(HttpStatusCode.OK, user.toUserDto())
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User with id '$id' does not exist.")
                    }
                }
                put {
                    val user = call.receive<UserDto>()
                    writeUserUseCase.update(user.toUser())
                    call.respond(HttpStatusCode.Accepted)
                }
                delete {
                    val id = call.request.queryParameters["id"]
                    val userIdentity = UUID.fromString(id).toIdentity()
                    writeUserUseCase.delete(userIdentity)
                    call.respond(HttpStatusCode.Accepted)
                }
                get("all") {
                    val users: List<User> = readUserUseCase.all()
                    call.respond(HttpStatusCode.OK, users.map { it.toUserDto() })
                }
            }
        }
    }
}
