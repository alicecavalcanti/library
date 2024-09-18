package com.challenge.library.model

import com.challenge.library.controller.dto.BookUpdateFormDTO
import org.bson.types.ObjectId

object BooksUpdateFormTest {
    fun build() = BookUpdateFormDTO(
        id = ObjectId("66eae95d7225161e6c7ae802"),
        titulo = "patolino22",
        resumo = "editdaldsakdlsakda dfkmdmfkdsmd fdkmfdsm fdksmfdsmf dsamkdsmakdmksamdks ",
        autor = "davi diego",
        ISBN = "1234567891234",
        categoria = mutableListOf("comédia", "jogos")
    )
}