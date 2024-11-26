package com.challenge.library.exception

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ResponseStatus

@Component
@ResponseStatus(
    HttpStatus.CONFLICT,
    reason = "O status do empréstimo do livro não está como CHECKED_OUT, por isso a requisição não pode ser feita"
)
class NotCheckedOutLoanException(
   override val message: String= "\$reason"
):RuntimeException()
