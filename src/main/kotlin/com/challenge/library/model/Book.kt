package com.challenge.library.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document(collection= "book")
data class Book(
    @Id
    var id: String?=null,
    var titulo: String,
    var resumo: String,
    var autor:String,
    var isbn: String,
    var categoria: List<String>,
    val notas: MutableList<Int> = mutableListOf(),
    var resenhas: MutableList<String> = mutableListOf()
): Serializable{

    fun updateWith(titulo: String, resumo: String, autor: String, isbn: String, categoria: List<String>){
        this.titulo = titulo
        this.resumo = resumo
        this.autor = autor
        this.isbn = isbn
        this.categoria = categoria
    }
}