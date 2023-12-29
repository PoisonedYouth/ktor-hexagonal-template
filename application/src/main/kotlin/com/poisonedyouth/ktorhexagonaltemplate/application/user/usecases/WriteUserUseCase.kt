package com.poisonedyouth.ktorhexagonaltemplate.application.user.usecases

import com.poisonedyouth.ktorhexagonaltemplate.common.vo.Identity
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.entity.User

interface WriteUserUseCase {
    fun add(user: User): Identity

    fun update(user: User)

    fun delete(userId: Identity)
}
