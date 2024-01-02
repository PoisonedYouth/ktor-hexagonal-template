package com.poisonedyouth.ktorhexagonaltemplate.test

import org.testcontainers.containers.PostgreSQLContainer

class KPostgreSQLContainer(image: String) : PostgreSQLContainer<KPostgreSQLContainer>(image)
