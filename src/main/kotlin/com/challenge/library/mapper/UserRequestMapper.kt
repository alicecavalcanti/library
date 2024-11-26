package com.challenge.library.mapper

import com.challenge.library.controller.dto.UserRequestDTO
import com.challenge.library.model.Roles
import com.challenge.library.model.Token
import com.challenge.library.model.User
import org.springframework.stereotype.Component

@Component
class UserRequestMapper(private val token: Token) : Mapper<UserRequestDTO, User>{


    override fun map(t: UserRequestDTO): User {
        return User(
            username = t.username,
            password = t.password,
            roles = listOf(Roles.ADMIN),
            token = token.TOKEN_ADMIN
        )
    }

    fun mapLibrary(t: UserRequestDTO): User{
        return User(
            username = t.username,
            password = t.password,
            roles = listOf(Roles.LIBRARY),
            token = token.TOKEN_LIBRARY
        )
    }

    fun mapMember(t: UserRequestDTO): User{
        return User(
            username = t.username,
            password = t.password,
            roles = listOf(Roles.MEMBER),
            token = token.TOKEN_MEMBER
        )
    }


}
