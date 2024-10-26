package com.challenge.library.model

object BooksTest{
    fun bookOne() = Book(
        titulo = "como eu era antes de você",
        resumo = "editdaldsakdlsakda dfkmdmfkdsmd fdkmfdsm fdksmfdsmf dsamkdsmakdmksamdks ",
        autor = "davi diego",
        isbn = "1234567891234",
        categoria = mutableListOf("comédia", "aventura"),
        notas=  mutableListOf(5,5,5,5,5)
    )


    fun bookTwo() = Book(
        titulo = "harry potter",
        resumo = "editdaldsakdlsakda dfkmdmfkdsmd fdkmfdsm fdksmfdsmf dsamkdsmakdmksamdks ",
        autor = "davi diego",
        isbn = "1234567891234",
        categoria = mutableListOf("ficção", "mistério"),
        notas=  mutableListOf(5,5,4,4,4)
    )
    fun bookThree() = Book(
        titulo = "senhor dos anéis",
        resumo = "editdaldsakdlsakda dfkmdmfkdsmd fdkmfdsm fdksmfdsmf dsamkdsmakdmksamdks ",
        autor = "davi diego",
        isbn = "1234567891234",
        categoria = mutableListOf("comédia", "animação"),
        notas=  mutableListOf(3,3,3,2,1)
    )
    fun bookFour() = Book(
        titulo = "patolino",
        resumo = "editdaldsakdlsakda dfkmdmfkdsmd fdkmfdsm fdksmfdsmf dsamkdsmakdmksamdks ",
        autor = "davi diego",
        isbn = "1234567891234",
        categoria = mutableListOf("comédia", "animação"),
        notas=  mutableListOf(1,2,3,2,1)
    )

 }