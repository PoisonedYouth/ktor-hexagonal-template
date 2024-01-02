package com.poisonedyouth.ktorhexagonaltemplate.domain.user.entity

import com.poisonedyouth.ktorhexagonaltemplate.common.vo.Identity
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.vo.Address
import com.poisonedyouth.ktorhexagonaltemplate.domain.user.vo.Name

data class User(val identity: Identity, val name: Name, val address: Address)
