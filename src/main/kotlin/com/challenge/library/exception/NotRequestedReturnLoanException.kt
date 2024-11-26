package com.challenge.library.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(
    HttpStatus.CONFLICT,
    reason = "O status do empréstimo do livro não é de um dos tipo de REQUESTED_RETURN, por isso a requisição não pode ser feita")
class NotRequestedReturnLoanException (
    override val message: String = "\$reason"
): RuntimeException()