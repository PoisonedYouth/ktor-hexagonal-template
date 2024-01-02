package com.poisonedyouth.ktorhexagonaltemplate.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import com.lemonappdev.konsist.api.ext.list.imports
import com.lemonappdev.konsist.api.ext.list.withPackage
import com.lemonappdev.konsist.api.verify.assertFalse
import org.junit.jupiter.api.Test

class ArchitectureTest {

    @Test
    fun `dependencies of modules are correct`() {
        Konsist.scopeFromProduction().assertArchitecture {
            val application =
                Layer("application", "com.poisonedyouth.ktorhexagonaltemplate.application..")
            val domain = Layer("domain", "com.poisonedyouth.ktorhexagonaltemplate.domain..")
            val infrastructure =
                Layer("infrastructure", "com.poisonedyouth.ktorhexagonaltemplate.infrastructure..")
            val bootstrap =
                Layer("bootstrap", "com.poisonedyouth.ktorhexagonaltemplate.bootstrap..")
            val common = Layer("common", "com.poisonedyouth.ktorhexagonaltemplate.common..")

            common.dependsOnNothing()
            domain.dependsOn(common)
            application.dependsOn(domain, common)
            infrastructure.dependsOn(application, domain, common)
            bootstrap.dependsOn(infrastructure)
        }
    }

    @Test
    fun `domain layer does not use Ktor framework`() {
        Konsist.scopeFromProduction()
            .files
            .withPackage("com.poisonedyouth.ktorhexagonaltemplate.domain..")
            .imports
            .assertFalse { import -> import.name.startsWith("io.ktor.") }
    }

    @Test
    fun `application player does not use infrastructure layer`() {
        Konsist.scopeFromProduction()
            .files
            .withPackage("com.poisonedyouth.ktorhexagonaltemplate.application..")
            .imports
            .assertFalse { import ->
                import.name.startsWith("com.poisonedyouth.ktorhexagonaltemplate.infrastructure")
            }
    }
}
