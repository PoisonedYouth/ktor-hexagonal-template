package com.poisonedyouth.ktorhexagonaltemplate.application.user.ports.input

import com.poisonedyouth.ktorhexagonaltemplate.application.user.ports.output.UserOutputPort
import com.poisonedyouth.ktorhexagonaltemplate.application.user.usecases.ReadUserUseCase
import com.poisonedyouth.ktorhexagonaltemplate.common.vo.Identity
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.entity.User

class ReadUserInputPort(private val userOutputPort: UserOutputPort) : ReadUserUseCase {
    override suspend fun find(userId: Identity): User? {
        return userOutputPort.findBy(userId)
    }

    override suspend fun all(): List<User> {
        return userOutputPort.all()
    }
}
