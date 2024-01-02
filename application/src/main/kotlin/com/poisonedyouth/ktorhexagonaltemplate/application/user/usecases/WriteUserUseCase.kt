package com.poisonedyouth.ktorhexagonaltemplate.application.user.usecases

import com.poisonedyouth.ktorhexagonaltemplate.common.vo.Identity
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.entity.User

interface WriteUserUseCase {
    suspend fun add(user: User): Identity

    suspend fun update(user: User)

    suspend fun delete(userId: Identity)
}
