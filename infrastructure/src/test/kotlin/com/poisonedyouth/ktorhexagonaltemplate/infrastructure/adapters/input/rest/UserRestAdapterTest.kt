package com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.input.rest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.poisonedyouth.ktorhexagonaltemplate.application.user.ports.output.UserOutputPort
import com.poisonedyouth.ktorhexagonaltemplate.application.user.usecases.ReadUserUseCase
import com.poisonedyouth.ktorhexagonaltemplate.common.vo.Identity
import com.poisonedyouth.ktorhexagonaltemplate.common.vo.toIdentity
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.entity.User
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.vo.Address
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.vo.Country
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.vo.Name
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.vo.ZipCode
import com.poisonedyouth.ktorhexagonaltemplate.test.executeApplicationTest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.jackson.jackson
import io.ktor.server.testing.ApplicationTestBuilder
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class UserRestAdapterTest {
    @Test
    fun `given non-existing user id, when findUser is called, return not found status`() =
        executeApplicationTest {
            val client = initHttpClient()

            val readUserUseCase: ReadUserUseCase = mock()
            application { loadKoinModules(module { single<ReadUserUseCase> { readUserUseCase } }) }

            // given
            val id = UUID.randomUUID().toIdentity()
            whenever(readUserUseCase.find(id)).thenReturn(null)

            // when
            val result =
                client.get("/api/v1/user") {
                    contentType(ContentType.Application.Json)
                    url { parameter("id", "${id.value}") }
                }

            // then
            assertThat(result.status).isEqualTo(HttpStatusCode.NotFound)
            assertThat(result.bodyAsText()).isEqualTo("User with id '${id.value}' does not exist.")
        }

    @Test
    fun `given existing user id, when findUser is called, then return user details`() =
        executeApplicationTest {
            val client = initHttpClient()

            val readUserUseCase: ReadUserUseCase = mock()

            application { loadKoinModules(module { single<ReadUserUseCase> { readUserUseCase } }) }

            // given
            val id = UUID.randomUUID().toIdentity()
            val user =
                User(
                    identity = id,
                    name = Name(firstName = "John", lastName = "Doe"),
                    address =
                        Address(
                            streetName = "Street",
                            streetNumber = "123",
                            city = "City",
                            zipCode = ZipCode(12346),
                            country = Country(name = "Country", code = "CR")
                        )
                )
            whenever(readUserUseCase.find(id)).thenReturn(user)

            // when
            val result =
                client.get("/api/v1/user") {
                    contentType(ContentType.Application.Json)
                    url { parameter("id", "${id.value}") }
                }

            // then
            assertThat(result.status).isEqualTo(HttpStatusCode.OK)
            assertThat(jacksonObjectMapper().readValue(result.bodyAsText(), UserDto::class.java))
                .isEqualTo(user.toUserDto())
        }

    @Test
    fun `given existing user id, when findUser is called without credentials, then not authorized is returned`() =
        executeApplicationTest {
            // given
            val readUserUseCase: ReadUserUseCase = mock()

            application { loadKoinModules(module { single<ReadUserUseCase> { readUserUseCase } }) }

            val id = UUID.randomUUID().toIdentity()
            val user =
                User(
                    identity = id,
                    name = Name(firstName = "John", lastName = "Doe"),
                    address =
                        Address(
                            streetName = "Street",
                            streetNumber = "123",
                            city = "City",
                            zipCode = ZipCode(12346),
                            country = Country(name = "Country", code = "CR")
                        )
                )
            whenever(readUserUseCase.find(id)).thenReturn(user)

            // when
            val result =
                client.get("/api/v1/user") {
                    contentType(ContentType.Application.Json)
                    url { parameter("id", "${id.value}") }
                }

            // then
            assertThat(result.status).isEqualTo(HttpStatusCode.Unauthorized)
        }

    @Test
    fun `given valid user data, when updateUser is called, then an existing user is updated`() =
        executeApplicationTest {
            // given
            val client = initHttpClient()

            val identity = Identity.UUIDIdentity.NEW
            val user =
                UserDto(
                    identity = identity.value,
                    firstName = "John",
                    lastName = "Doe",
                    address =
                        AddressDto(
                            streetName = "Street",
                            streetNumber = "123",
                            city = "City",
                            zipCode = 12345,
                            country = CountryDto(name = "Country", code = "CR")
                        )
                )

            val userOutputPort: UserOutputPort = mock()

            application { loadKoinModules(module { single<UserOutputPort> { userOutputPort } }) }
            whenever(userOutputPort.findBy(identity)).thenReturn(user.toUser())

            // when
            val result =
                client.put("/api/v1/user") {
                    contentType(ContentType.Application.Json)
                    setBody(user)
                }

            // then
            assertThat(result.status).isEqualTo(HttpStatusCode.Accepted)
        }

    @Test
    fun `given invalid user data, when updateUser is called, then a bad request is returned`() =
        executeApplicationTest {
            // given
            val client = initHttpClient()

            val identity = Identity.UUIDIdentity.NEW
            val user =
                UserDto(
                    identity = identity.value,
                    firstName = "John",
                    lastName = "Doe",
                    address =
                        AddressDto(
                            streetName = "Street",
                            streetNumber = "123",
                            city = "City",
                            zipCode = 123456, // Invalid
                            country = CountryDto(name = "Country", code = "CR")
                        )
                )

            // when
            val result =
                client.put("/api/v1/user") {
                    contentType(ContentType.Application.Json)
                    setBody(user)
                }

            // then
            assertThat(result.status).isEqualTo(HttpStatusCode.BadRequest)
        }

    @Test
    fun `given existing userId, when deleteUser is called, then accepted is returned`() =
        executeApplicationTest {
            // given
            val client = initHttpClient()

            val identity = Identity.UUIDIdentity.NEW
            val user =
                UserDto(
                    identity = identity.value,
                    firstName = "John",
                    lastName = "Doe",
                    address =
                        AddressDto(
                            streetName = "Street",
                            streetNumber = "123",
                            city = "City",
                            zipCode = 12345,
                            country = CountryDto(name = "Country", code = "CR")
                        )
                )

            val userOutputPort: UserOutputPort = mock()

            application { loadKoinModules(module { single<UserOutputPort> { userOutputPort } }) }
            whenever(userOutputPort.findBy(identity)).thenReturn(user.toUser())

            // when
            val result =
                client.delete("/api/v1/user") {
                    contentType(ContentType.Application.Json)
                    url { parameter("id", "${identity.value}") }
                }

            // then
            assertThat(result.status).isEqualTo(HttpStatusCode.Accepted)
        }

    @Test
    fun `given not existing userId, when deleteUser is called, then not found is returned`() =
        executeApplicationTest {
            // given
            val client = initHttpClient()
            val identity = Identity.UUIDIdentity.NEW

            // when
            val result =
                client.delete("/api/v1/user") {
                    contentType(ContentType.Application.Json)
                    url { parameter("id", "${identity.value}") }
                }

            // then
            assertThat(result.status).isEqualTo(HttpStatusCode.NotFound)
        }

    @Test
    fun `delete user when user not send authorization header, return not authorized`() =
        executeApplicationTest {
            // given
            val identity = Identity.UUIDIdentity.NEW

            // when
            val result =
                client.delete("/api/v1/user") {
                    contentType(ContentType.Application.Json)
                    url { parameter("id", "${identity.value}") }
                }

            // then
            assertThat(result.status).isEqualTo(HttpStatusCode.Unauthorized)
        }

    private fun ApplicationTestBuilder.initHttpClient() = createClient {
        install(ContentNegotiation) { jackson() }
        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(username = "ValidUsername", password = "0123456789")
                }
            }
        }
    }
}
