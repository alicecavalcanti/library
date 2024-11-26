package com.challenge.library.exception

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ResponseStatus

@Component
@ResponseStatus(HttpStatus.CONFLICT, reason = "O livro não está fora do prazo de entrega")
class BookNotLateException(
    override val message: String = "\$reason"
) : RuntimeException(){
}