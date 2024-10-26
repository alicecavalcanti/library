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

    private val listBooks = listOf(BooksTest.bookOne(), BooksTest.bookTwo(), BooksTest.bookThree(),
    BooksTest.bookFour()
    )

    @BeforeEach
    fun setup(){
        bookRepository.deleteAll()
    }


    @Test
    fun `deve conseguir pesquisar pelo titulo, autor, isbn ou categoria, passando um desses valores para a variável search do livro armazenado no banco`(){
        bookRepository.save(listBooks.get(0))

        val search = "patolino"
        val book = bookRepository.findBook(search, PageRequest.of(0, 5))

        Assertions.assertTrue(!book.isEmpty)

     }

    @Test
    fun `Não deve conseguir pesquisar pelo titulo, autor, isbn ou categoria, pois a pesquisa é feita por um livro que não está no banco`(){
        bookRepository.save(listBooks.get(0))

        val search = "mouse"
        val book = bookRepository.findBook(search, PageRequest.of(0, 5))

        Assertions.assertTrue(!book.isEmpty)

    }
//    @Test
//    fun `deve conseguir informar duas categorias e a quantidade 1 para total de empréstimos`(){
//        bookRepository.save(listBooks.get(0))
//
//        val amountLoans =bookRepository.findAmountLoansCategory()
//
//        Assertions.assertEquals(amountLoans.get(0).category, "comédia")
//        Assertions.assertEquals(amountLoans.get(0).totalLoans, 1)
//    }
//
//    @Test
//    fun `Não deve retornar a categorias e a quantidade de empréstimos totais, retornando um erro`(){
//        // sem o livro salvo
//        val amountLoans =bookRepository.findAmountLoansCategory()
//
//        Assertions.assertEquals(amountLoans.get(0).category, "comédia")
//        Assertions.assertEquals(amountLoans.get(0).totalLoans, 3)
//    }

    @Test
    fun  `Deve retornar a média das melhores notas em ordem decrescente`(){
        val bestGrade = listBooks.get(0)
        val secondBestGrade =listBooks.get(1)
        val thirdBestGrade =listBooks.get(2)
        val fourthBestGrade =listBooks.get(3)

        bookRepository.save(fourthBestGrade)
        bookRepository.save(thirdBestGrade)
        bookRepository.save(secondBestGrade)
        bookRepository.save(bestGrade)

        val amountBestNotes = bookRepository.findBestBookNotes()

        Assertions.assertEquals(bestGrade.id, amountBestNotes.get(0).idBook)
        Assertions.assertEquals(secondBestGrade.id, amountBestNotes.get(1).idBook)
        Assertions.assertEquals(thirdBestGrade.id, amountBestNotes.get(2).idBook)
        Assertions.assertEquals(fourthBestGrade.id, amountBestNotes.get(3).idBook)

    }

}