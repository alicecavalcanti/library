package com.challenge.library.data

import com.challenge.library.model.Book

object ListBooksData{
    fun build() = listOf(
        Book(
            id = "1",
            titulo = "senhor dos anéis",
            resumo = "editdaldsakdlsakda dfkmdmfkdsmd fdkmfdsm fdksmfdsmf dsamkdsmakdmksamdks ",
            autor = "davi diego",
            isbn = "1234567891234",
            categoria = mutableListOf("comédia", "aventura"),
            notas=  mutableListOf(5,5,5,5,5)
        ),
        Book(
            id = "2",
            titulo = "harry potter",
            resumo = "editdaldsakdlsakda dfkmdmfkdsmd fdkmfdsm fdksmfdsmf dsamkdsmakdmksamdks ",
            autor = "Lucas Silva",
            isbn = "1234567891234",
            categoria = mutableListOf("ficção", "mistério"),
            notas=  mutableListOf(5,5,4,4,4)
        ),
        Book(
            id = "3",
            titulo = "Moana",
            resumo = "editdaldsakdlsakda dfkmdmfkdsmd fdkmfdsm fdksmfdsmf dsamkdsmakdmksamdks ",
            autor = "davi diego",
            isbn = "1234567891234",
            categoria = mutableListOf("comédia", "animação"),
            notas=  mutableListOf(3,3,3,2,1)
        ),
        Book(
            id = "4",
            titulo = "patolino",
            resumo = "editdaldsakdlsakda dfkmdmfkdsmd fdkmfdsm fdksmfdsmf dsamkdsmakdmksamdks ",
            autor = "davi diego",
            isbn = "1234567891234",
            categoria = mutableListOf("comédia", "animação"),
            notas=  mutableListOf(1,2,3,2,1)
        )
    )
 }