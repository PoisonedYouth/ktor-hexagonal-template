package com.poisonedyouth.ktorhexagonaltemplate.application.user.ports.output

import com.poisonedyouth.ktorhexagonaltemplate.common.vo.Identity
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.entity.User

interface UserOutputPort {

    suspend fun store(user: User): Identity

    suspend fun findBy(identity: Identity): User?

    suspend fun delete(identity: Identity)

    suspend fun all(): List<User>
}
