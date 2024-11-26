package com.challenge.library.model

import com.challenge.library.controller.dto.BookUpdateRequestDTO
import org.bson.types.ObjectId

object BooksUpdateFormTest {
    fun build() = BookUpdateRequestDTO(
        id = "id",
        titulo = "patolino22",
        resumo = "editdaldsakdlsakda dfkmdmfkdsmd fdkmfdsm fdksmfdsmf dsamkdsmakdmksamdks ",
        autor = "davi diego",
        isbn = "1234567891234",
        categoria = mutableListOf("comédia", "jogos")

    )
}