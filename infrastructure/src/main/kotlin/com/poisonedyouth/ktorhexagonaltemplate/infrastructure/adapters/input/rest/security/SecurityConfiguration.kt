package com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.input.rest.security

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.basic
import org.koin.ktor.ext.inject

fun Application.installBasicAuthentication() {
    val userDetailsService by inject<UserDetailsService>()

    install(Authentication) {
        basic("auth-basic") {
            validate { credentials ->
                val existingUser = userDetailsService.loadUserByUsername(credentials.name)
                if (existingUser.password == credentials.password) {
                    existingUser
                } else {
                    null
                }
            }
        }
    }
}
