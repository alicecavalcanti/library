package com.challenge.library.model

import com.challenge.library.controller.dto.BookRequestDTO

object BooksFormTest {
    fun build() = BookRequestDTO(
        titulo = "Livro de teste2",
        resumo = "editdaldsakdlsakda dfkmdmfkdsmd fdkmfdsm fdksmfdsmf dsamkdsmakdmksamdks ",
        autor = "davi diego",
        isbn = "1234567891234",
        categoria = mutableListOf("comédia", "jogos")
    )
}