package com.challenge.library.exception

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ResponseStatus

@Component
@ResponseStatus(HttpStatus.FORBIDDEN, reason = "Você não pode criar um usuário com essa role" )
class UserCreationNotAllowedException(
    override val message: String? = null
) : RuntimeException()
