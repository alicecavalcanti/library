package com.challenge.library.exception

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ResponseStatus

@Component
@ResponseStatus(HttpStatus.CONFLICT, reason= "Não pode ser feita a solicitação de empréstimo, porque este livro já foi solicitado")
class BookCannotBeRequestedException (
    override val message: String = "\$reason"
): RuntimeException()