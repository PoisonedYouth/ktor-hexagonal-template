package com.poisonedyouth.ktorhexagonaltemplate.test

import com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.output.exposed.flyway.configureAndMigrateDatabase
import com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.output.exposed.initializeDatabaseConnection
import com.poisonedyouth.ktorhexagonaltemplate.infrastructure.configuration.installKoin
import com.poisonedyouth.ktorhexagonaltemplate.infrastructure.configuration.main
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import org.koin.core.context.stopKoin

fun <R> executeApplicationTest(applicationTestBuilder: suspend ApplicationTestBuilder.() -> R) =
    testApplication {
        environment {
            config =
                MapApplicationConfig(
                    "database.url" to postgres.getJdbcUrl(),
                    "database.driver-classname" to postgres.driverClassName,
                    "database.username" to postgres.username,
                    "database.password" to postgres.password,
                    "database.max-pool-size" to "3"
                )
        }
        application { main() }
        applicationTestBuilder.invoke(this).also { stopKoin() }
    }

fun <R> executePersistenceTest(applicationTestBuilder: suspend ApplicationTestBuilder.() -> R) =
    testApplication {
        environment {
            config =
                MapApplicationConfig(
                    "database.url" to postgres.getJdbcUrl(),
                    "database.driver-classname" to postgres.driverClassName,
                    "database.username" to postgres.username,
                    "database.password" to postgres.password,
                    "database.max-pool-size" to "3"
                )
        }
        application {
            installKoin()
            initializeDatabaseConnection()
            configureAndMigrateDatabase()
        }
        startApplication()
        applicationTestBuilder.invoke(this).also { stopKoin() }
    }
