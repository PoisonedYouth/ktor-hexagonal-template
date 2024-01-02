package com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.output.exposed

import com.poisonedyouth.ktorhexagonaltemplate.application.user.ports.output.UserOutputPort
import com.poisonedyouth.ktorhexagonaltemplate.common.vo.Identity
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.entity.User
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.vo.Address
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.vo.Country
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.vo.Name
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.vo.ZipCode
import com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.output.exposed.tables.AddressTable
import com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.output.exposed.tables.CountryTable
import com.poisonedyouth.ktorhexagonaltemplate.infrastructure.adapters.output.exposed.tables.UserTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update

class ExposedUserRepository : UserOutputPort {
    override suspend fun store(user: User): Identity = newSuspendedTransaction {
        val countryRow =
            CountryTable.select { CountryTable.code eq user.address.country.code }.singleOrNull()
        val countryId =
            if (countryRow == null) {
                CountryTable.insertAndGetId {
                    it[name] = user.address.country.name
                    it[code] = user.address.country.code
                }
            } else {
                countryRow[CountryTable.id]
            }

        val addressId =
            AddressTable.insertAndGetId {
                it[city] = user.address.city
                it[streetName] = user.address.streetName
                it[streetNumber] = user.address.streetNumber
                it[zipCode] = user.address.zipCode.value
                it[country] = countryId
            }

        val id =
            when (user.identity) {
                Identity.NoIdentity -> {
                    UserTable.insertAndGetId {
                            it[firstname] = user.name.firstName
                            it[lastname] = user.name.lastName
                            it[address] = addressId
                        }
                        .value
                }
                is Identity.UUIDIdentity -> {
                    UserTable.update({
                        UserTable.id eq (user.identity as Identity.UUIDIdentity).value
                    }) {
                        it[firstname] = user.name.firstName
                        it[lastname] = user.name.lastName
                        it[address] = addressId
                    }
                    (user.identity as Identity.UUIDIdentity).value
                }
            }
        Identity.UUIDIdentity(id)
    }

    override suspend fun findBy(identity: Identity): User? = newSuspendedTransaction {
        if (identity is Identity.NoIdentity) {
            null
        } else {
            UserTable.select { UserTable.id eq identity.getId() }.singleOrNull()?.toUser()
        }
    }

    override suspend fun delete(identity: Identity) = newSuspendedTransaction {
        UserTable.deleteWhere { UserTable.id eq identity.getId() }
        Unit
    }

    override suspend fun all(): List<User> = newSuspendedTransaction {
        UserTable.selectAll().map { it.toUser() }
    }

    private fun ResultRow.toUser(): User {
        val addressRow =
            AddressTable.select { AddressTable.id eq this@toUser[UserTable.address] }.single()
        val countryRow =
            CountryTable.select { CountryTable.id eq addressRow[AddressTable.country] }.single()
        return User(
            identity = Identity.UUIDIdentity(this@toUser[UserTable.id].value),
            name = Name(this@toUser[UserTable.firstname], this@toUser[UserTable.lastname]),
            address =
                Address(
                    city = addressRow[AddressTable.city],
                    streetName = addressRow[AddressTable.streetName],
                    streetNumber = addressRow[AddressTable.streetNumber],
                    zipCode = ZipCode(addressRow[AddressTable.zipCode]),
                    country = Country(countryRow[CountryTable.name], countryRow[CountryTable.code])
                )
        )
    }
}
