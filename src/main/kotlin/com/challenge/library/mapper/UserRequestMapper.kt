package com.challenge.library.mapper

import com.challenge.library.controller.dto.SignUpRequestDTO
import com.challenge.library.model.User
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class UserRequestMapper : Mapper<SignUpRequestDTO, User>{
    override fun map(t: SignUpRequestDTO): User {
        return User(
            username = t.username,
            name = t.name,
            password = t.password,
            registrationDate = LocalDate.now(),
            roles = t.role
        )
    }
}
