plugins {
    alias(libs.plugins.flyway)
    id("pitest-configuration")
    id("java-test-fixtures")
}
dependencies {
    implementation(project(":domain"))
    implementation(project(":common"))
    implementation(project(":application"))

    // Ktor
    implementation(libs.ktorJackson)
    implementation(libs.ktorContentNegotiation)
    implementation(libs.jacksonJavaDateTime)
    implementation(libs.ktorStatusPages)
    implementation(libs.ktorServerAuth)
    implementation(libs.kotlinReflect)

    // Logging
    implementation(libs.logback)

    // Koin
    implementation(libs.koinKtor)
    implementation(libs.koinKtorLogger)

    // Exposed
    implementation(libs.exposedJavaTime)
    implementation(libs.exposedJdbc)
    implementation(libs.exposedCore)

    //Persistence
    runtimeOnly(libs.postgresqlConnector)
    implementation(libs.hikari)

    // Flyway
    implementation(libs.flyway)
    implementation(libs.flywayPostgresql)

    // OpenAPI
    implementation(libs.ktorSwagger)
    implementation(libs.ktorOpenApi)

    // Testing
    testImplementation(libs.junitJupiterEngine)
    testImplementation(libs.junitJupiterApi)
    testImplementation(libs.kotlinMockito)
    testRuntimeOnly(libs.flywayPostgresql)

    testImplementation(libs.ktorTest)
    testImplementation(libs.ktorClientAuth)
    testImplementation(libs.ktorClientContentNegotiation)
    testImplementation(libs.koinTest)
    testImplementation(libs.koinJunit5)
    testImplementation(libs.assertJ)
    testImplementation(libs.coroutinesTest)

    testFixturesImplementation(libs.testContainersCore)
    testFixturesImplementation(libs.testContainersPostgresql)
    testFixturesImplementation(libs.testContainersJunitJupiter)
    testFixturesImplementation(libs.ktorTest)
    testFixturesImplementation(libs.ktorClientAuth)
    testFixturesImplementation(libs.ktorClientContentNegotiation)
    testFixturesImplementation(libs.junitJupiterApi)
    testFixturesImplementation(libs.koinTest)
    testFixturesImplementation(libs.koinJunit5)
}