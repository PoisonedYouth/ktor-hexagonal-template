package com.poisonedyouth.ktorhexagonaltemplate.application.user.ports.output

import com.poisonedyouth.ktorhexagonaltemplate.common.vo.Identity
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.entity.User

interface UserOutputPort {

    fun store(user: User): Identity

    fun findBy(identity: Identity): User?

    fun delete(identity: Identity)

    fun all(): List<User>
}
