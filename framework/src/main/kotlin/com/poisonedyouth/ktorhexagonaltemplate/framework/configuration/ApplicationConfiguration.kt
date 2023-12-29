package com.poisonedyouth.ktorhexagonaltemplate.framework.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.poisonedyouth.ktorhexagonaltemplate.application.user.ports.input.ReadUserInputPort
import com.poisonedyouth.ktorhexagonaltemplate.application.user.ports.input.WriteUserInputPort
import com.poisonedyouth.ktorhexagonaltemplate.application.user.ports.output.UserOutputPort
import com.poisonedyouth.ktorhexagonaltemplate.application.user.usecases.ReadUserUseCase
import com.poisonedyouth.ktorhexagonaltemplate.application.user.usecases.WriteUserUseCase
import com.poisonedyouth.ktorhexagonaltemplate.framework.adapters.output.exposed.ExposedUserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfiguration {

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().registerModules(KotlinModule.Builder().build(), JavaTimeModule())
    }

    @Bean fun userOutputPort(): UserOutputPort = ExposedUserRepository()

    @Bean
    fun writeUserUseCase(
        userOutputPort: UserOutputPort,
    ): WriteUserUseCase = WriteUserInputPort(userOutputPort)

    @Bean
    fun readUserUseCase(userOutputPort: UserOutputPort): ReadUserUseCase =
        ReadUserInputPort(userOutputPort)
}
