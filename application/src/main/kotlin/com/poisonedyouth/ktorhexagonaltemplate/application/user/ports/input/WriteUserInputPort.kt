package com.poisonedyouth.ktorhexagonaltemplate.application.user.ports.input

import com.poisonedyouth.ktorhexagonaltemplate.application.user.ports.output.UserOutputPort
import com.poisonedyouth.ktorhexagonaltemplate.application.user.usecases.WriteUserUseCase
import com.poisonedyouth.ktorhexagonaltemplate.common.exception.NotFoundException
import com.poisonedyouth.ktorhexagonaltemplate.common.vo.Identity
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.entity.User

class WriteUserInputPort(
    private val userOutputPort: UserOutputPort,
) : WriteUserUseCase {
    override suspend fun add(user: User): Identity {
        return userOutputPort.store(user)
    }

    override suspend fun update(user: User) {
        requireNotNull(userOutputPort.findBy(user.identity)) {
            "User with id '${user.identity} does not exist."
        }
        userOutputPort.store(user)
    }

    override suspend fun delete(userId: Identity) {
        userOutputPort.findBy(userId)
            ?: throw NotFoundException("User with id '${userId.getId()} does not exist.")
        userOutputPort.delete(userId)
    }
}
