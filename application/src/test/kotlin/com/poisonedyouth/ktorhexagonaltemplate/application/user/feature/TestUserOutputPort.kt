package com.poisonedyouth.ktorhexagonaltemplate.application.user.feature

import com.poisonedyouth.ktorhexagonaltemplate.application.user.ports.output.UserOutputPort
import com.poisonedyouth.ktorhexagonaltemplate.common.vo.Identity
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.entity.User

class TestUserOutputPort : UserOutputPort {
    private val userList = mutableListOf<User>()

    override suspend fun store(user: User): Identity {
        val newUser = user.copy(identity = Identity.UUIDIdentity.NEW)
        userList.add(newUser)
        return newUser.identity
    }

    override suspend fun findBy(identity: Identity): User? {
        return userList.firstOrNull { it.identity == identity }
    }

    override suspend fun delete(identity: Identity) {
        userList.removeIf { it.identity == identity }
    }

    override suspend fun all(): List<User> {
        return userList.toList()
    }
}
