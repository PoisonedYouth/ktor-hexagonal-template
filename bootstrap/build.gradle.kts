import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
   alias(libs.plugins.springboot)
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlinSpring)
}

dependencies {
    implementation(project(":framework"))

    // Spring
    implementation(libs.springBootStarterWeb)
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springBootStarterTestcontainers)

    // OpenAPI
    implementation(libs.springdocOpenApi)

    // Testing
    testImplementation(libs.testContainersCore)
    testImplementation(libs.testContainersPostgresql)
    testImplementation(libs.testContainersJunitJupiter)
}

springBoot {
    mainClass = "com.poisonedyouth.ktorhexagonaltemplate.bootstrap.KtorHexagonalTemplateApplicationKt"
}

tasks.withType<BootBuildImage> {
    imageName = "ktor-hexagonal-template:0.0.1"
}