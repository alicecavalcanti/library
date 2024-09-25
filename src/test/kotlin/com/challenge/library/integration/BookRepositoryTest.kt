package com.challenge.library.integration

import com.challenge.library.configuration.AbstractContainerizedMongoTest
import com.challenge.library.model.BooksTest
import com.challenge.library.repository.BookRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest


class BookRepositoryTest : AbstractContainerizedMongoTest(){

    @Autowired
    lateinit var bookRepository: BookRepository

    private val books = BooksTest.build()

    @BeforeEach
    fun setup(){
        bookRepository.deleteAll()
    }


    @Test
    fun `must be able to consult the 4 forms of member user search`(){
        bookRepository.save(books)

        val search = "davi diego"
        val bookReturn = bookRepository.findAllByAutor(search, PageRequest.of(0, 5))

        Assertions.assertTrue(bookReturn.content[0].autor.equals(search))

     }

}