package com.challenge.library.mapper

import com.challenge.library.controller.dto.BookFormDTO
import com.challenge.library.model.Books
import org.bson.types.ObjectId
import org.springframework.stereotype.Component
import java.util.*

@Component
class BooksFormMapper : Mapper<BookFormDTO, Books>{
    override fun map(t: BookFormDTO) : Books {
        return Books(
            id = ObjectId(),
            titulo = t.titulo.lowercase(),
            resumo = t.resumo.lowercase(),
            autor = t.autor.lowercase(),
            categoria = t.categoria.map { t-> t.lowercase() },
            ISBN = t.ISBN
        )
    }
}