package com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.output.exposed

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database

fun Application.initializeDatabaseConnection() {
    val config = HikariConfig()
    config.driverClassName = environment.config.property("database.driver-classname").getString()
    config.jdbcUrl = environment.config.property("database.url").getString()
    config.username = environment.config.property("database.username").getString()
    config.password = environment.config.property("database.password").getString()
    config.isAutoCommit = true
    config.maximumPoolSize =
        environment.config.property("database.max-pool-size").getString().toInt()
    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    config.validate()
    val datasource = HikariDataSource(config)
    Database.connect(datasource)
}
