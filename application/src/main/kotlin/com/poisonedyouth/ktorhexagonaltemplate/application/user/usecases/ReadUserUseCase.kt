package com.poisonedyouth.ktorhexagonaltemplate.application.user.usecases

import com.poisonedyouth.ktorhexagonaltemplate.common.vo.Identity
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.entity.User

interface ReadUserUseCase {
    fun find(userId: Identity): User?

    fun all(): List<User>
}
