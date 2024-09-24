package com.challenge.library.repository

import com.challenge.library.model.Book
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface BookRepository : MongoRepository<Book, String>{
    // TODO: alterar query para regex
    @Query("{'\$or':[ {'titulo': ?0, {\$regex: /^ABC/i} }, {'autor': ?0,  {\$regex: /^ABC/i}}, {'ISBN': ?0}, {'categoria': ?0, }, {\$regex:/^ABC/i} ] }")
    fun findBook(search: String, pagination: Pageable): Page<Book>
}