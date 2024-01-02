package com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.input.rest.security

import io.ktor.server.auth.Principal
import java.nio.file.attribute.UserPrincipalNotFoundException

data class UserPrincipal(
    val username: String,
    val password: String,
) : Principal

interface UserDetailsService {
    fun loadUserByUsername(username: String): UserPrincipal
}

class DefaultUserDetailsService : UserDetailsService {
    override fun loadUserByUsername(username: String): UserPrincipal {
        // Load user from database
        val existingUser = findUser(username)

        return existingUser
            ?: throw UserPrincipalNotFoundException("User with username '${username}' not found.")
    }

    private fun findUser(username: String): UserPrincipal? {
        return if (username == "ValidUsername") {
            UserPrincipal(username = username, password = "0123456789")
        } else {
            null
        }
    }
}
