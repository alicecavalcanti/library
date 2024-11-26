package com.challenge.library.repository

import com.challenge.library.data.ListLoanData
import com.challenge.library.model.Loan
import com.challenge.library.model.LoanStatus
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
import java.time.LocalDate
import java.time.LocalTime

@DataMongoTest
@Testcontainers
class LoanRepositoryTest {

    companion object {
        @JvmStatic
        @Container
        @ServiceConnection
        var mongoDBContainer = MongoDBContainer("mongo:latest")
            .withExposedPorts(27017)
    }

    @Autowired
    lateinit var loanRepository: LoanRepository

    val listLoanData = ListLoanData.build()

    @BeforeEach
    fun setup(){
        loanRepository.deleteAll()
    }

    @Test
    fun `Quando consultar os emprestimo feitos, então deve encontrar todos os empréstimo com o status requested loan`(){
        val loan1 = Loan(
            id = "1",
            idUser = "user1",
            idBook = "book1",
            status = LoanStatus.REQUESTED_LOAN,
            loanDate = LocalDate.of(2024,5,10),
            expectedReturnDate = LocalDate.of(2024, 11, 10),
            userReturnDate = LocalDate.of(2024, 10, 18)
        )
        val loan2 = Loan(
            id = "2",
            idUser = "user2",
            idBook = "book2",
            status = LoanStatus.REQUESTED_LOAN,
            loanDate = LocalDate.of(2024,5,20),
            expectedReturnDate = LocalDate.of(2024, 10, 20),
            userReturnDate = LocalDate.of(2024, 10, 18)
        )

        val loans = listOf(loan1, loan2)

        loanRepository.saveAll(loans)

        val loansFound = loanRepository.findLoans(PageRequest.of(0,5))

        Assertions.assertThat(loansFound.content)
            .isNotEmpty
            .satisfies({
                    Assertions.assertThat(it.size).isEqualTo(2)
                    Assertions.assertThat(it.map{ it.id }).containsExactlyInAnyOrder("1", "2")
                    Assertions.assertThat(it.map { it.status }).allMatch({status -> status.equals(LoanStatus.REQUESTED_LOAN)})
                    Assertions.assertThat(it.map { it.idUser }).containsExactlyInAnyOrder("user1", "user2")
                    Assertions.assertThat(it.map { it.idBook }).containsExactlyInAnyOrder("book1", "book2")
            })

    }

    @Test
    fun `Quando consultar os emprestimo feitos, então não deve encontrar nenhum empréstimo`(){
        val loans = listOf(
            listLoanData[2],
            listLoanData[3],
            listLoanData[4],
            listLoanData[5]
        )
        loanRepository.saveAll(loans)

        val loansFound = loanRepository.findLoans(PageRequest.of(0,5))

        Assertions.assertThat(loansFound.content).isEmpty()
    }

    @Test
    fun `Quando os empréstimos for pego por período, então deve retornar 6 emprestimos do período`(){

        loanRepository.saveAll(listLoanData)

        val start = LocalDate.of(2024,4,1).atTime(LocalTime.now())
        val end = LocalDate.of(2024, 6,1).atTime(LocalTime.now())
        val loan = loanRepository.findLoansByPeriod(start, end)

        Assertions.assertThat(loan.size).isEqualTo(listLoanData.size)
    }

    @Test
    fun `Quando os empréstimos for pego por período, então deve retornar vazio`(){

        loanRepository.saveAll(listLoanData)

        val start = LocalDate.of(2024,1,1).atTime(LocalTime.now())
        val end = LocalDate.of(2024, 2,1).atTime(LocalTime.now())
        val loan = loanRepository.findLoansByPeriod(start, end)

        Assertions.assertThat(loan).isEmpty()
    }

    @Test
    fun `Quando os empréstimos for pego por período e ele iniciar depois do fim, então deve retornar vazio a lista`(){

        loanRepository.saveAll(listLoanData)

        val start = LocalDate.of(2024,6,1).atTime(LocalTime.now())
        val end = LocalDate.of(2024, 4,1).atTime(LocalTime.now())
        val loan = loanRepository.findLoansByPeriod(start, end)

        Assertions.assertThat(loan).isEmpty()
    }

    @Test
    fun `Quando encontrar os 3 livros mais emprestados, então deve retornar os 5`(){
        val book1 = listLoanData[0].idBook
        val book2 = listLoanData[3].idBook
        val book3 = listLoanData[5].idBook

        loanRepository.saveAll(listLoanData)

        val mostRequestedBooks = loanRepository.threeMostBorrowedBooks()

        val topBook1 = mostRequestedBooks[0]
        val topBook2 = mostRequestedBooks[1]
        val topBook3 = mostRequestedBooks[2]

        Assertions.assertThat(book1).isEqualTo(topBook1.idBook)
        Assertions.assertThat(topBook1.totalLoans).isEqualTo(3)
        Assertions.assertThat(book2).isEqualTo(topBook2.idBook)
        Assertions.assertThat(topBook2.totalLoans).isEqualTo(2)
        Assertions.assertThat(book3).isEqualTo(topBook3.idBook)
        Assertions.assertThat(topBook3.totalLoans).isEqualTo(1)
    }

    @Test
    fun `Quando encontrar os 3 livros mais solicitados, deve retornar vazio`(){
        val mostRequestedBooks = loanRepository.threeMostBorrowedBooks()

        Assertions.assertThat(mostRequestedBooks).isEmpty()
    }

    @Test
    fun `Quando chamar para encontrar todas as devoluções dos empréstimos, então dele retornar uma emprestimo que tem o status RETURNED`(){
        loanRepository.saveAll(listLoanData)
        val loansReturns = loanRepository.findAllLoansReturns()

        Assertions.assertThat(loansReturns.size).isEqualTo(1)
        Assertions.assertThat(loansReturns[0].status).isEqualTo(LoanStatus.RETURNED)
        Assertions.assertThat(loansReturns[0].id).isEqualTo("4")
    }

    @Test
    fun `Quando chamar para encontrar todas as devoluções dos empréstimos e nenhuma tiver o status do tipo RETURNED, então dele retornar vazio`(){
        val loans = listOf(listLoanData[0], listLoanData[1])

        loanRepository.saveAll(loans)
        val result = loanRepository.findAllLoansReturns()

        Assertions.assertThat(result).isEmpty()
    }
}