package com.poisonedyouth.ktorhexagonaltemplate.framework.adapters.output

import org.testcontainers.containers.PostgreSQLContainer

internal class KPostgreSQLContainer(image: String) :
    PostgreSQLContainer<KPostgreSQLContainer>(image)
