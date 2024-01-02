dependencies{
    testRuntimeOnly(project(":domain"))
    testRuntimeOnly(project(":application"))
    testRuntimeOnly(project(":infrastructure"))
    testImplementation(libs.konsist)
    testImplementation(libs.junitJupiterEngine)
    testImplementation(libs.junitJupiterApi)
}