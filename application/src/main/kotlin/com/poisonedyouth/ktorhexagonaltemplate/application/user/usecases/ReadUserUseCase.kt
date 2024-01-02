package com.poisonedyouth.ktorhexagonaltemplate.application.user.usecases

import com.poisonedyouth.ktorhexagonaltemplate.common.vo.Identity
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.entity.User

interface ReadUserUseCase {
    suspend fun find(userId: Identity): User?

    suspend fun all(): List<User>
}
