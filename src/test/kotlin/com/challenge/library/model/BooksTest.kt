package com.challenge.library.model

import org.bson.types.ObjectId

 object BooksTest{
    fun build() = Books(
        id = ObjectId(),
        titulo = "patolino",
        resumo = "editdaldsakdlsakda dfkmdmfkdsmd fdkmfdsm fdksmfdsmf dsamkdsmakdmksamdks ",
        autor = "davi diego",
        ISBN = "1234567891234",
        categoria = mutableListOf("comédia", "jogos")
    )
 }