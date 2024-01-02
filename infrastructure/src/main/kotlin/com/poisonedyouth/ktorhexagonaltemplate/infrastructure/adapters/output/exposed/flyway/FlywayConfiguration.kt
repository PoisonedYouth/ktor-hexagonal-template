package com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.output.exposed.flyway

import io.ktor.server.application.Application
import org.flywaydb.core.Flyway

fun Application.configureAndMigrateDatabase() {
    val flyway =
        Flyway.configure()
            .dataSource(
                environment.config.property("database.url").getString(),
                environment.config.property("database.username").getString(),
                environment.config.property("database.password").getString()
            )
            .load()
    flyway.migrate()
}
