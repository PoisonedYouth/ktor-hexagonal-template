package com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.output.exposed

import com.poisonedyouth.ktorhexagonaltemplate.application.user.ports.output.UserOutputPort
import com.poisonedyouth.ktorhexagonaltemplate.common.vo.Identity
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.entity.User
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.vo.Address
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.vo.Country
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.vo.Name
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.vo.ZipCode
import com.poisonedyouth.ktorhexagonaltemplate.test.executePersistenceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.java.KoinJavaComponent.inject
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
internal class ExposedUserRepositoryTest {

    @Test
    fun `storeUser should store user and return correct identity`() = executePersistenceTest {
        val userRepository by getKoin().inject<UserOutputPort>()

        // given
        val user =
            User(
                identity = Identity.NoIdentity,
                name = Name("John", "Doe"),
                address =
                    Address(
                        streetName = "Street Name",
                        streetNumber = "Street Number",
                        city = "City",
                        zipCode = ZipCode(12345),
                        country = Country(name = "United States", code = "USA")
                    )
            )
        // when
        val actual = userRepository.store(user)
        // then
        assertThat(actual).isInstanceOf(Identity.UUIDIdentity::class.java)
    }

    @Test
    fun `findUserBy should return null when no user is found`() = executePersistenceTest {
        val userRepository by getKoin().inject<UserOutputPort>()

        // given
        val identity = Identity.UUIDIdentity.NEW
        // when
        val actual = userRepository.findBy(identity)
        // then
        assertThat(actual).isNull()
    }

    @Test
    fun `findUserBy should return correct user when user is found`() = executePersistenceTest {
        val userRepository by getKoin().inject<UserOutputPort>()

        // given
        val user =
            User(
                identity = Identity.NoIdentity,
                name = Name("John", "Doe"),
                address =
                    Address(
                        streetName = "Street Name",
                        streetNumber = "Street Number",
                        city = "City",
                        zipCode = ZipCode(12345),
                        country = Country("United States", "USA")
                    )
            )

        val userId = userRepository.store(user)

        // when
        val actual = userRepository.findBy(userId)
        // then
        assertThat(actual).isNotNull()
        assertThat(actual).isEqualTo(user.copy(identity = userId))
    }
}
