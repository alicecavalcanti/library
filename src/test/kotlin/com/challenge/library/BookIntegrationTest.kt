package com.challenge.library

import com.challenge.library.controller.dto.BookFeedbackDTO
import com.challenge.library.controller.dto.BookRequestDTO
import com.challenge.library.controller.dto.BookUpdateRequestDTO
import com.challenge.library.data.ListBooksData
import com.challenge.library.model.Book
import com.challenge.library.repository.BookRepository
import com.challenge.library.service.BookService
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BookIntegrationTest {

    companion object {
        @JvmStatic
        @Container
        @ServiceConnection
        var mongoDBContainer = MongoDBContainer("mongo:latest")
            .withExposedPorts(27017)
    }

    private val RECURSO = "/book"

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var bookRepository : BookRepository

    @Autowired
    private lateinit var bookService: BookService

    val listBooksData =  ListBooksData.build()

    @BeforeEach
    fun setup(){
        bookRepository.deleteAll()
    }

    @Test
    fun `Quando pesquisar um livro por uma propriedade, então deve retornar todos os que tem essa propriedade search`(){
        bookRepository.saveAll(listBooksData)

        mockMvc.perform(
            get(RECURSO)
                .param("search", "Lucas Silva")
                .param("page", "0")
                .param("size", "10")

        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.total_elements").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].id").value(listBooksData[1].id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].titulo").value(listBooksData[1].titulo))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].autor").value(listBooksData[1].autor))
    }

    @Test
    fun `Quando listar todos os livros, então deve retornar todos os livros criados`(){
        bookRepository.saveAll(listBooksData)

        mockMvc.perform(
            get(RECURSO.plus("/catalog"))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.total_elements").value(4))

    }

    @Test
    fun `Quando registrar um livro, então deve retornar esse livros cadastrada`(){
        val bookRequest = BookRequestDTO(
        titulo = "Livro de teste2",
        resumo = "editdaldsakdlsakda dfkmdmfkdsmd fdkmfdsm fdksmfdsmf dsamkdsmakdmksamdks ",
        autor = "davi diego",
        isbn = "1234567891234",
        categoria = mutableListOf("comédia", "jogos")
        )
        mockMvc.perform(
            post(RECURSO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookRequest))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.titulo").value(bookRequest.titulo))
            .andExpect(MockMvcResultMatchers.jsonPath("$.resumo").value(bookRequest.resumo))
            .andExpect(MockMvcResultMatchers.jsonPath("$.autor").value(bookRequest.autor))
            .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(bookRequest.isbn))
    }
    
    @Test
    fun `Quando fizer a ação de editar um livro, então deve retornar o livro alterado`(){
        val book = Book(
            id = "id1",
            titulo = "patolino22",
            resumo = "editdaldsakdlsakda dfkmdmfkdsmd fdkmfdsm fdksmfdsmf dsamkdsmakdmksamdks ",
            autor = "davi diego",
            isbn = "1234567891234",
            categoria = mutableListOf("comédia", "jogos"),
            notas=  mutableListOf(5,5,5,5,5)
        )
        val bookUpdateRequest = BookUpdateRequestDTO(
            id = "id1",
            titulo = "patolino",
            resumo = "É um filme de 1h40, que conta a história de patolino ",
            autor = "Luís Rodrigues",
            isbn = "1234567891234",
            categoria = mutableListOf("comédia", "jogos")
        )

        bookRepository.save(book)

        mockMvc.perform(
            put(RECURSO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookUpdateRequest))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.titulo").value(bookUpdateRequest.titulo))
            .andExpect(MockMvcResultMatchers.jsonPath("$.autor").value(bookUpdateRequest.autor))
            .andExpect(MockMvcResultMatchers.jsonPath("$.resumo").value(bookUpdateRequest.resumo))
    }

    @Test
    fun `Quando fizer a ação de deletar um livro, então ele não deve mais existir e retornar vazio e como NoContent`(){
        val book = Book(
            id = "id1",
            titulo = "patolino22",
            resumo = "editdaldsakdlsakda dfkmdmfkdsmd fdkmfdsm fdksmfdsmf dsamkdsmakdmksamdks ",
            autor = "davi diego",
            isbn = "1234567891234",
            categoria = mutableListOf("comédia", "jogos"),
            notas=  mutableListOf(5,5,5,5,5)
        )
        bookRepository.save(book)

        mockMvc.perform(
            delete(RECURSO.plus("/{id}"), "id1")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent)

        Assertions.assertThat(bookRepository.findById(book.id!!)).isEmpty
    }

    @Test
    fun `Quando fazer a ação de dar um feedback, então ele deve retornar o feedback`(){
        val book = Book(
            id = "1",
            titulo = "patolino",
            resumo = "editdaldsakdlsakda dfkmdmfkdsmd fdkmfdsm fdksmfdsmf dsamkdsmakdmksamdks ",
            autor = "davi diego",
            isbn = "1234567891234",
            categoria = mutableListOf("comédia", "jogos")
        )

        bookRepository.save(book)

        val feedback = BookFeedbackDTO(
            "1",
            "Livro muito bom, cenas muito bem feitas. O filme é bem divertido e engraçado na medida certa",
            5
        )

        mockMvc.perform(
            post(RECURSO.plus("/feedback-book"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(feedback))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(book.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.notas").value(feedback.nota))
            .andExpect(MockMvcResultMatchers.jsonPath("$.resenhas").value(feedback.resenha))
    }
    
}