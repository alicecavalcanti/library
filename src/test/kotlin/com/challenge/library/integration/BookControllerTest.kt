package com.challenge.library.integration


import com.challenge.library.configuration.ConfigurationContainerizedRedisTest
import com.challenge.library.model.BooksFormTest
import com.challenge.library.model.BooksUpdateFormTest

import com.challenge.library.service.BookService
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(ConfigurationContainerizedRedisTest::class)
class BookControllerTest{
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var bookService: BookService


    private val booksForm= BooksFormTest.build()

    private val books = BooksUpdateFormTest.build()

    companion object{
        private var RECURSO = "/books"
    }

    @Throws(JsonProcessingException::class)
    fun toJsonBookForm(): String {
        val mapper: ObjectMapper = ObjectMapper()
        return mapper.writeValueAsString(booksForm)
    }
    @Throws(JsonProcessingException::class)
    fun toJsonBook(): String {
        val mapper: ObjectMapper = ObjectMapper()
        return mapper.writeValueAsString(books)
    }

    @Test
    fun `should search only by title and return an object of type book`(){
        mockMvc.get(RECURSO.plus("?titulo=testando%20cache5")).andExpect { status { is2xxSuccessful() }
        }
    }

    @Test
    fun `You must be able to search by title, author, ISBN and return a list of books found`(){
        mockMvc.get(RECURSO.plus("/member?search=testando%20cache5")).andExpect { status { is2xxSuccessful() }
        }
    }

    @Test
    fun `must register a book`(){
        mockMvc.perform(post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJsonBookForm()))
            .andExpect(status().isCreated)
    }

    @Test
    fun `should edit a book`(){
        mockMvc.perform(put("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJsonBook()))
            .andExpect(status().isOk)
    }

    @Test
    fun `should delete the book`(){
        mockMvc.perform(delete("/books".plus("/" + books.id))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent)
    }


}
