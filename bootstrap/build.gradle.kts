plugins {
   alias(libs.plugins.ktor)
    id ("java-test-fixtures")
}

dependencies {
    implementation(project(":infrastructure"))

    // Ktor
    implementation(libs.ktorCIOEngine)
    implementation(libs.ktorServerCore)

    // Testing
    testImplementation(libs.ktorTest)
    testImplementation(libs.junitJupiterApi)
    testImplementation(libs.junitJupiterEngine)
    testImplementation(libs.assertJ)
    testImplementation(testFixtures(project(":infrastructure")))
}

ktor {
    docker {
        localImageName.set("ktor-hexagonal-template")
        imageTag.set("0.0.1")
    }
}

application {
    mainClass.set("com.poisonedyouth.ktorhexagonaltemplate.bootstrap.KtorHexagonalTemplateApplication")
}