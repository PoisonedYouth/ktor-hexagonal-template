package com.poisonedyouth.ktorhexagonaltemplate.infrastructure.configuration

import com.poisonedyouth.ktorhexagonaltemplate.application.user.ports.input.ReadUserInputPort
import com.poisonedyouth.ktorhexagonaltemplate.application.user.ports.input.WriteUserInputPort
import com.poisonedyouth.ktorhexagonaltemplate.application.user.ports.output.UserOutputPort
import com.poisonedyouth.ktorhexagonaltemplate.application.user.usecases.ReadUserUseCase
import com.poisonedyouth.ktorhexagonaltemplate.application.user.usecases.WriteUserUseCase
import com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.input.rest.security.DefaultUserDetailsService
import com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.input.rest.security.UserDetailsService
import com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.output.exposed.ExposedUserRepository
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val applicationModule = module {
    single<UserOutputPort> { ExposedUserRepository() }

    single<WriteUserUseCase> { WriteUserInputPort(get()) }

    single<ReadUserUseCase> { ReadUserInputPort(get()) }

    single<UserDetailsService> { DefaultUserDetailsService() }
}

fun Application.installKoin() {
    install(Koin) {
        slf4jLogger()
        modules(applicationModule)
    }
}
