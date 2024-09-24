package com.challenge.library.mapper

import com.challenge.library.controller.dto.BookRequestDTO
import com.challenge.library.model.Book
import org.springframework.stereotype.Component

@Component
class BooksRequestMapper : Mapper<BookRequestDTO, Book>{
    override fun map(t: BookRequestDTO) : Book {
        return Book(
            titulo = t.titulo,
            resumo = t.resumo,
            autor = t.autor,
            categoria = t.categoria,
            ISBN = t.ISBN
        )
    }
}