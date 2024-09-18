package com.challenge.library.repository

import com.challenge.library.model.Books
import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository


interface BookRepository : MongoRepository<Books, ObjectId>{
    fun findAllByTitulo(titulo: String, pagination: Pageable): Page<Books>
    fun findAllByAutor(autor: String, pagination: Pageable): Page<Books>
    fun findAllByISBN(ISBN: String, pagination: Pageable): Page<Books>
    fun findAllByCategoria(categoria: String, pagination: Pageable): Page<Books>
}