package com.challenge.library.mapper

import com.challenge.library.controller.dto.UserRequestDTO
import com.challenge.library.model.Roles
import com.challenge.library.model.User
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class UserRequestMapper : Mapper<UserRequestDTO, User>{


    override fun map(t: UserRequestDTO): User {
        return User(
            username = t.username,
            password = t.password,
            registrationDate = LocalDate.now(),
            roles = listOf(Roles.ADMIN)

        )
    }

    fun mapLibrary(t: UserRequestDTO): User{
        return User(
            username = t.username,
            password = t.password,
            registrationDate = LocalDate.now(),
            roles = listOf(Roles.LIBRARY)
        )
    }

    fun mapMember(t: UserRequestDTO): User{
        return User(
            username = t.username,
            password = t.password,
            registrationDate = LocalDate.now(),
            roles = listOf(Roles.MEMBER)
        )
    }
}
