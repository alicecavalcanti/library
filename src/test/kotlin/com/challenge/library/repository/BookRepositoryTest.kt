package com.challenge.library.repository

import com.challenge.library.data.ListBooksData
import com.challenge.library.model.Book
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.data.domain.PageRequest
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@DataMongoTest
@Testcontainers
class BookRepositoryTest {

    companion object {
        @JvmStatic
        @Container
        @ServiceConnection
        var mongoDBContainer = MongoDBContainer("mongo:latest")
            .withExposedPorts(27017)
    }

    @Autowired
    lateinit var bookRepository: BookRepository

    private val listBooks = ListBooksData.build()

//    companion object {
//        @JvmStatic
//        @AfterAll
//        fun tearDown() {
//            BookIntegrationTest.mongoDBContainer.stop()
//        }
//    }

    @BeforeEach
    fun setup() {
        bookRepository.deleteAll()
    }

    @Test
    fun `deve conseguir pesquisar pelo titulo, autor, isbn ou categoria, passando um desses valores para a variável search do livro armazenado no banco`() {
        bookRepository.saveAll(listBooks)

        val search = "SENHOR dos anÉis"
        val book = bookRepository.findBook(search, PageRequest.of(0, 10))

        Assertions.assertThat(book.content)
            .isNotNull
            .satisfies({
                Assertions.assertThat(it.size).isEqualTo(1)
                Assertions.assertThat(it[0].id).isEqualTo(listBooks[0].id)
                Assertions.assertThat(it[0].titulo).isEqualTo(listBooks[0].titulo)
            })

    }

    @Test
    fun `Não deve conseguir pesquisar pelo titulo, autor, isbn ou categoria, pois a pesquisa é feita por um livro que não está no banco`() {
        bookRepository.saveAll(listBooks)

        val search = "mouse"
        val book = bookRepository.findBook(search, PageRequest.of(0, 5))

        Assertions.assertThat(book.content).isEmpty()
    }

    @Test
    fun `Deve retornar a média das melhores notas em ordem decrescente`() {
        val bestGrade = listBooks[0]
        val secondBestGrade = listBooks[1]
        val thirdBestGrade = listBooks[2]
        val fourthBestGrade = listBooks[3]

        val books = listOf(bestGrade, secondBestGrade, thirdBestGrade, fourthBestGrade)
        bookRepository.saveAll(books)

        val amountBestNotes = bookRepository.findBestBookNotes()

        Assertions.assertThat(bestGrade.id).isEqualTo(amountBestNotes[0].idBook)
        Assertions.assertThat(secondBestGrade.id).isEqualTo(amountBestNotes[1].idBook)
        Assertions.assertThat(thirdBestGrade.id).isEqualTo(amountBestNotes[2].idBook)
        Assertions.assertThat(fourthBestGrade.id).isEqualTo(amountBestNotes[3].idBook)
    }

    @Test
    fun `Quando pegar as melhores avaliações, então deve retornar vazio`() {
        val book1 = Book(
            id = "1",
            titulo = "senhor dos anéis",
            resumo = "editdaldsakdlsakda dfkmdmfkdsmd fdkmfdsm fdksmfdsmf dsamkdsmakdmksamdks ",
            autor = "davi diego",
            isbn = "1234567891234",
            categoria = mutableListOf("comédia", "aventura")
        )
        val book2 = Book(
            id = "2",
            titulo = "harry potter",
            resumo = "editdaldsakdlsakda dfkmdmfkdsmd fdkmfdsm fdksmfdsmf dsamkdsmakdmksamdks ",
            autor = "Lucas Silva",
            isbn = "1234567891234",
            categoria = mutableListOf("ficção", "mistério")
        )
        val books = listOf(book1, book2)

        bookRepository.saveAll(books)
        val amountBestNotes = bookRepository.findBestBookNotes()

        Assertions.assertThat(amountBestNotes).isEmpty()
    }
}