package com.challenge.library.service


import com.challenge.library.data.ListBooksData
import com.challenge.library.data.LoanBookDTOData
import com.challenge.library.exception.BookAlreadyRequestedException
import com.challenge.library.exception.NotApprovedLoanException
import com.challenge.library.exception.NotCheckedOutLoanException
import com.challenge.library.exception.NotRequestedReturnLoanException
import com.challenge.library.mapper.LoanRequestMapper
import com.challenge.library.model.Loan
import com.challenge.library.model.LoanStatus
import com.challenge.library.repository.LoanRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
class LoanServiceTest {

    @MockK
    private lateinit var loanRepository: LoanRepository

    @MockK
    private lateinit var bookService: BookService

    @MockK
    private lateinit var loanRequestMapper: LoanRequestMapper

    @InjectMockKs
    lateinit var loanService: LoanService

    @Test
    fun `Quando fizer a ação de registrar o empréstimo, então o empréstimo criado deve ser retornado`() {
        val loanBookDTO = LoanBookDTOData.build()
        val book = ListBooksData.build().get(0)

        val loanObject = Loan(
            idBook = "1",
            idUser = "5",
            status = LoanStatus.REQUESTED_LOAN,
            expectedReturnDate = LocalDate.of(2024, 10, 28)
        )

        val loans = listOf(
            Loan(
                id = "1",
                idUser = "User1",
                idBook = "Book1",
                status = LoanStatus.RETURNED,
                expectedReturnDate = LocalDate.of(2024, 12, 1)
            ),
            Loan(
                id = "2",
                idUser = "User2",
                idBook = "Book2",
                status = LoanStatus.RETURNED,
                expectedReturnDate = LocalDate.of(2024, 12, 15),
                userReturnDate = LocalDate.of(2024, 12, 14)
            ),
            Loan(
                id = "3",
                idUser = "User3",
                idBook = "Book3",
                status = LoanStatus.RETURNED,
                expectedReturnDate = LocalDate.of(2024, 11, 20),
                userReturnDate = LocalDate.of(2024, 11, 18)
            )
        )

        every { bookService.findBookById(book.id!!) } returns book
        every { loanRepository.findAllByIdBook(loanBookDTO.idBook) } returns loans
        every { loanRequestMapper.map(loanBookDTO) } returns loanObject
        every { loanRepository.save(loanObject) } returns loanObject

        val loan = loanService.registerLoad(loanBookDTO)

        Assertions.assertThat(loan)
            .isNotNull
            .satisfies({
                Assertions.assertThat(loan).isInstanceOf(Loan::class.java)
                Assertions.assertThat(it.idBook).isEqualTo(loanObject.idBook)
                Assertions.assertThat(it.idUser).isEqualTo(loanObject.idUser)
                Assertions.assertThat(it.status).isEqualTo(loanObject.status)
                Assertions.assertThat(it.expectedReturnDate).isEqualTo(loanObject.expectedReturnDate)
            })
    }

    @Test
    fun `Quando fizer a ação de registrar o empréstimo, então o empréstimo não deve ser criado`() {
        val loanBookDTO = LoanBookDTOData.build()
        val book = ListBooksData.build().get(0)

        val loans = listOf(
            Loan(
                id = "1",
                idUser = "User1",
                idBook = "Book1",
                status = LoanStatus.RETURNED,
                expectedReturnDate = LocalDate.of(2024, 12, 1)
            ),
            Loan(
                id = "2",
                idUser = "User2",
                idBook = "Book2",
                status = LoanStatus.REQUESTED_LOAN,
                expectedReturnDate = LocalDate.of(2024, 12, 15),
                userReturnDate = LocalDate.of(2024, 12, 14)
            ),
            Loan(
                id = "3",
                idUser = "User3",
                idBook = "Book3",
                status = LoanStatus.RETURNED,
                expectedReturnDate = LocalDate.of(2024, 11, 20),
                userReturnDate = LocalDate.of(2024, 11, 18)
            )
        )

        every { bookService.findBookById(book.id!!) } returns book
        every { loanRepository.findAllByIdBook(loanBookDTO.idBook) } returns loans

        assertThatThrownBy { loanService.registerLoad(loanBookDTO) }.isInstanceOf(BookAlreadyRequestedException::class.java)
    }

    @Test
    fun `Quando fizer a ação de aprovar um empréstimo, então deve aprovar e retornar o livro solicitado o empréstimo`() {
        val loanObject = Loan(
            id = "1",
            idBook = "1",
            idUser = "5",
            status = LoanStatus.REQUESTED_LOAN,
            expectedReturnDate = LocalDate.of(2024, 10, 28)
        )

        every { loanService.findLoanById(loanObject.id!!) } returns loanObject
        every { loanRepository.save(loanObject) } answers {
            firstArg<Loan>().apply {
                id = ObjectId().toString()
            }
        }

        val loan = loanService.approveLoanRequest(loanObject.id!!)

        Assertions.assertThat(loan)
            .isNotNull
            .satisfies({
                Assertions.assertThat(loan.id).isEqualTo(loanObject.id)
                Assertions.assertThat(loan.idBook).isEqualTo(loanObject.idBook)
                Assertions.assertThat(loan.idUser).isEqualTo(loanObject.idUser)
                Assertions.assertThat(loan.status).isEqualTo(LoanStatus.APPROVED)
                Assertions.assertThat(loan.expectedReturnDate).isEqualTo(loanObject.expectedReturnDate)
            })
    }

    @Test
    fun `Quando fizer a ação de aprovar um empréstimo, então deve lançar uma exception`() {
        val loanObject = Loan(
            id = "1",
            idBook = "1",
            idUser = "5",
            status = LoanStatus.APPROVED,
            expectedReturnDate = LocalDate.of(2024, 10, 28)
        )

        every { loanService.findLoanById(loanObject.id!!) } returns loanObject

        assertThatThrownBy { loanService.approveLoanRequest(loanObject.id!!) }.isInstanceOf(NotApprovedLoanException::class.java)
    }

    @Test
    fun `Quando aprovar o retorno, então deve ser retornado o livro que foi devolvido`(){
        val loanObject = Loan(
            id = "1",
            idBook = "1",
            idUser = "5",
            status = LoanStatus.REQUESTED_RETURN,
            expectedReturnDate = LocalDate.of(2024, 10, 28)
        )

        every { loanService.findLoanById(loanObject.id!!) } returns loanObject
        every { loanRepository.save(loanObject) } answers {
            firstArg<Loan>().apply{
                id = ObjectId().toString()
            }
        }

        val loan = loanService.approveReturnRequest(loanObject.id!!)

        Assertions.assertThat(loan)
            .isNotNull
            .isInstanceOf(Loan::class.java)
            .satisfies({
                Assertions.assertThat(loan.id).isEqualTo(loanObject.id)
                Assertions.assertThat(loan.idBook).isEqualTo(loanObject.idBook)
                Assertions.assertThat(loan.idUser).isEqualTo(loanObject.idUser)
                Assertions.assertThat(loan.status).isEqualTo(LoanStatus.RETURNED)
                Assertions.assertThat(loan.expectedReturnDate).isEqualTo(loanObject.expectedReturnDate)
            })
    }

    @Test
    fun `quando aprovar o retorno, então deve ser retornado uma exception`(){
        val loanObject = Loan(
            id = "1",
            idBook = "1",
            idUser = "5",
            status = LoanStatus.REQUESTED_LOAN,
            expectedReturnDate = LocalDate.of(2024, 10, 28)
        )

        every { loanService.findLoanById(loanObject.id!!) } returns loanObject

        assertThatThrownBy { loanService.approveReturnRequest(loanObject.id!!)}.isInstanceOf( NotRequestedReturnLoanException::class.java)
    }

    @Test
    fun `Quando fizer uma ação de pegar um livro, então o empréstimo do livro pego`(){
        val loanObject = Loan(
            id = "1",
            idBook = "1",
            idUser = "5",
            status = LoanStatus.APPROVED,
            expectedReturnDate = LocalDate.of(2024, 10, 28)
        )

        every { loanService.findLoanById(loanObject.id!!) } returns loanObject
        every { loanRepository.save(loanObject) } answers {
            firstArg<Loan>()
        }

       val loan =  loanService.grabLoanBook(loanObject.id!!)

        Assertions.assertThat(loan)
            .isNotNull
            .isInstanceOf(Loan::class.java)
            .satisfies({
              Assertions.assertThat(loan.id).isEqualTo(loanObject.id)
              Assertions.assertThat(loan.idBook).isEqualTo(loanObject.idBook)
              Assertions.assertThat(loan.idUser).isEqualTo(loanObject.idUser)
              Assertions.assertThat(loan.status).isEqualTo(LoanStatus.CHECKED_OUT)
              Assertions.assertThat(loan.expectedReturnDate).isEqualTo(loanObject.expectedReturnDate)
        })
    }

    @Test
    fun `Quando fizer a ação de pegar o livro, então deve ser lançada uma exception`(){
        val loanObject = Loan(
            id = "1",
            idBook = "1",
            idUser = "5",
            status = LoanStatus.REQUESTED_LOAN,
            expectedReturnDate = LocalDate.of(2024, 10, 28)
        )

        every { loanService.findLoanById(loanObject.id!!) } returns loanObject

        assertThatThrownBy { loanService.grabLoanBook(loanObject.id!!) }.isInstanceOf(NotCheckedOutLoanException::class.java)
    }

    @Test
    fun `Quando eu fizer a ação de requisitar a devolução do livro, então de retonar uma lista de 3 livros devolvidos nesse tempo`(){
        val loanObject = Loan(
            id = "1",
            idBook = "1",
            idUser = "5",
            status = LoanStatus.CHECKED_OUT,
            expectedReturnDate = LocalDate.of(2024, 10, 28)
        )

        every { loanService.findLoanById(loanObject.id!!) } returns loanObject
        every { loanRepository.save(loanObject) } answers {
            firstArg<Loan>()
        }

        val loan = loanService.bookReturn(loanObject.id!!)

        Assertions.assertThat(loan)
            .isNotNull
            .isInstanceOf(Loan::class.java)
            .satisfies({
                Assertions.assertThat(loan.id).isEqualTo(loanObject.id)
                Assertions.assertThat(loan.idBook).isEqualTo(loanObject.idBook)
                Assertions.assertThat(loan.idUser).isEqualTo(loanObject.idUser)
                Assertions.assertThat(loan.status).isEqualTo(LoanStatus.REQUESTED_RETURN)
                Assertions.assertThat(loan.expectedReturnDate).isEqualTo(loanObject.expectedReturnDate)
            })
    }

    @Test
    fun `Quando eu fizer a ação de requisitar a devolução do livro, então de retonar uma exception`(){
        val loanObject = Loan(
            id = "1",
            idBook = "1",
            idUser = "5",
            status = LoanStatus.REQUESTED_LOAN,
            expectedReturnDate = LocalDate.of(2024, 10, 28)
        )

        every { loanService.findLoanById(loanObject.id!!) } returns loanObject

        assertThatThrownBy { loanService.bookReturn(loanObject.id!!) } .isInstanceOf(NotRequestedReturnLoanException::class.java)
    }

    @Test
    fun  `Quando encontrar todos os empréstimos em um mês, então deve retornar o totais de empréstimos desse mês`(){
        val listLoan = listOf(
             Loan (
                id = "1",
                idBook = "1",
                idUser = "5",
                status = LoanStatus.REQUESTED_LOAN,
                expectedReturnDate = LocalDate.of(2024, 10, 28),
                userReturnDate =  LocalDate.of(2024, 10, 30)
            ),

            Loan (
                id = "1",
                idBook = "1",
                idUser = "5",
                status = LoanStatus.REQUESTED_LOAN,
                expectedReturnDate = LocalDate.of(2024, 10, 28),
                userReturnDate =  LocalDate.of(2024, 10, 30)
            ),

            Loan (
                id = "1",
                idBook = "1",
                idUser = "5",
                status = LoanStatus.REQUESTED_LOAN,
                expectedReturnDate = LocalDate.of(2024, 10, 28),
                userReturnDate =  LocalDate.of(2024, 10, 30)
            )
        )

        every { loanRepository.findLoansByPeriod(any(), any()) } returns listLoan

        val listLoanOne = loanService.findAllLoanMonth()

        Assertions.assertThat(listLoanOne.size).isEqualTo(3)
    }
}