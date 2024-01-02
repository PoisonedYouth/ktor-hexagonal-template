package com.poisonedyouth.ktorhexagonaltemplate.test

import org.flywaydb.core.Flyway
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.containers.PostgreSQLContainer

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ExtendWith(ClearDatabaseExtension::class)
annotation class WithDatabase

val postgres: PostgreSQLContainer<*> = KPostgreSQLContainer("postgres:16-alpine").apply { start() }

val flyway: Flyway =
    Flyway.configure()
        .cleanDisabled(false)
        .dataSource(postgres.getJdbcUrl(), postgres.username, postgres.password)
        .load()

object ClearDatabaseExtension : BeforeEachCallback {
    override fun beforeEach(context: ExtensionContext?) {
        flyway.clean()
        flyway.migrate()
    }
}
