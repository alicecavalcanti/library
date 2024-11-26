package com.challenge.library.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT, reason = "Empréstimo não encontrado")
class LoanNotFoundException(
    override val message: String = "\${reason}"
): RuntimeException()