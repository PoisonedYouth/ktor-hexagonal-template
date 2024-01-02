package com.poisonedyouth.ktorhexagonaltemplate.application.user.ports.input

import com.poisonedyouth.ktorhexagonaltemplate.application.user.ports.output.UserOutputPort
import com.poisonedyouth.ktorhexagonaltemplate.common.vo.toIdentity
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.entity.User
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.vo.Address
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.vo.Country
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.vo.Name
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.vo.ZipCode
import java.util.*
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class ReadUserInputPortTest {

    private val userOutputPort: UserOutputPort = mock()

    private val readUserInputPort = ReadUserInputPort(userOutputPort)

    @Test
    fun `should return userDto when user exists`() = runTest {
        // Given
        val userId = UUID.randomUUID().toIdentity()
        val user =
            User(
                identity = userId,
                name = Name("John", "Doe"),
                address =
                    Address(
                        "Street",
                        "123",
                        "City",
                        ZipCode(12345),
                        Country("United States", "USA")
                    )
            )
        whenever(userOutputPort.findBy(userId)).thenReturn(user)

        // When
        val result = readUserInputPort.find(userId)

        // Then
        assertThat(result).isNotNull
        assertThat(result?.identity).isEqualTo(userId)
        assertThat(result?.name?.firstName).isEqualTo("John")
        assertThat(result?.name?.lastName).isEqualTo("Doe")
    }

    @Test
    fun `should return null when user does not exist`() = runTest {
        // Given
        val userId = UUID.randomUUID().toIdentity()
        whenever(userOutputPort.findBy(userId)).thenReturn(null)

        // When
        val result = readUserInputPort.find(userId)

        // Then
        assertThat(result).isNull()
    }
}
