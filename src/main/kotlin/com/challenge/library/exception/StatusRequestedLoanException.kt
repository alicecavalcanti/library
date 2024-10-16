package com.challenge.library.exception

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(
    HttpStatus.CONFLICT,
    reason = "O status do empréstimo do livro não é de um dos tipo de REQUESTED_LOAN, por isso a requisição não pode ser feita")
class StatusRequestedLoanException(
    override val message: String = "\$reason"
): RuntimeException()