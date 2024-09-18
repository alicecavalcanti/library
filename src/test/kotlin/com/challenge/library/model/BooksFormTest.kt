package com.challenge.library.model

import com.challenge.library.controller.dto.BookFormDTO

object BooksFormTest {
    fun build() = BookFormDTO(
        titulo = "Livro de teste2",
        resumo = "editdaldsakdlsakda dfkmdmfkdsmd fdkmfdsm fdksmfdsmf dsamkdsmakdmksamdks ",
        autor = "davi diego",
        ISBN = "1234567891234",
        categoria = mutableListOf("comédia", "jogos")
    )
}