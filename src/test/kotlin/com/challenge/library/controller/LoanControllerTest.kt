package com.challenge.library.controller

import com.challenge.library.controller.dto.LoanBookDTO
import com.challenge.library.model.Loan
import com.challenge.library.model.LoanStatus
import com.challenge.library.service.LoanService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate


@WebMvcTest(controllers = [LoanController::class])
class LoanControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var loanService : LoanService

    val paginacao =PageRequest.of(0,10)

    @Test
    fun `Quando listar todos os empréstimos, então deve retornar 200Ok`() {
        val  loan = listOf(Loan(
            id = "1",
            idUser = "user1",
            idBook = "book1",
            status = LoanStatus.REQUESTED_LOAN,
            expectedReturnDate = LocalDate.of(2024, 11, 10),
            userReturnDate = LocalDate.of(2024, 10, 18)
        ),
        Loan(
            id = "2",
            idUser = "user2",
            idBook = "book2",
            status = LoanStatus.REQUESTED_LOAN,
            expectedReturnDate = LocalDate.of(2024, 11, 10),
            userReturnDate = LocalDate.of(2024, 10, 18)
        ))

        val pageableLoans = PageImpl(loan)

        every { loanService.listAllLibraryLoans(paginacao) } returns pageableLoans

        mockMvc.perform(
            get("/list").param("page", "0").param("size", "10")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.total_elements").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].id").value(containsInAnyOrder(loan.get(0).id,loan.get(1).id)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].id_user").value(containsInAnyOrder(loan.get(0).idUser,loan.get(1).idUser)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].id_book").value(containsInAnyOrder(loan.get(0).idBook,loan.get(1).idBook)))
    }

    @Test
    fun `Quando listar todos os empréstimos de um usuário, então deve retornar 200Ok`() {
        val  loan = listOf(Loan(
            id = "1",
            idUser = "user1",
            idBook = "book1",
            status = LoanStatus.REQUESTED_LOAN,
            expectedReturnDate = LocalDate.of(2024, 11, 10),
            userReturnDate = LocalDate.of(2024, 10, 18)
            ),

            Loan(
                id = "2",
                idUser = "user1",
                idBook = "book2",
                status = LoanStatus.REQUESTED_LOAN,
                expectedReturnDate = LocalDate.of(2024, 11, 10),
                userReturnDate = LocalDate.of(2024, 10, 18)
            )
        )

        val pageableLoans = PageImpl(loan)

        every { loanService.listUserBookLoan("user1", paginacao) } returns pageableLoans

        mockMvc.perform(
            get("/list/{idUser}", "user1").param("page", "0").param("size","10")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `Quando registrar todos os empréstimos, então deve retornar 201created e o conteúdo deles`() {
        val loan = Loan(
            id = "1",
            idBook = "book1",
            idUser = "user1",
            status = LoanStatus.REQUESTED_LOAN,
            expectedReturnDate = LocalDate.of(2024, 10, 28),
            userReturnDate = LocalDate.of(2024, 10, 30)
        )
        val loanBookDTO = LoanBookDTO(
            idBook = loan.idBook,
            idUser = loan.idUser,
            expectedReturnDate = loan.expectedReturnDate!!
        )

        every { loanService.registerLoad(match<LoanBookDTO>{ it.idBook.equals(loan.idBook) && it.idUser.equals(loan.idUser) }) } returns loan
            mockMvc.perform(
                post("/register")
                    .content(objectMapper.writeValueAsString(loanBookDTO))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id_user").value("user1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id_book").value("book1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(LoanStatus.REQUESTED_LOAN.name))
    }

    @Test
    fun `Quando aprovar um empréstimo, então deve retornar 200ok`() {
        val loan = Loan(
            id = "1",
            idBook = "book1",
            idUser = "user1",
            status = LoanStatus.REQUESTED_LOAN,
            expectedReturnDate = LocalDate.of(2024, 10, 28),
            userReturnDate = LocalDate.of(2024, 10, 30)
        )

        every { loanService.approveLoanRequest(loan.id!!) } returns loan

        mockMvc.perform(
            put("/approve/{idLoan}", "1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(loan.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id_user").value(loan.idUser))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id_book").value(loan.idBook))
    }

    @Test
    fun `Quando aprovar a devolução, então deve retornar 200ok`() {
        val loan = Loan(
            id = "1",
            idBook = "book2",
            idUser = "user2",
            status = LoanStatus.REQUESTED_LOAN,
            expectedReturnDate = LocalDate.of(2024, 10, 28),
            userReturnDate = LocalDate.of(2024, 10, 30)
        )
        every {
            loanService.approveReturnRequest("1")
        } returns loan


        mockMvc.perform(
            put("/approve-return/{idLoan}", "1")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(loan.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id_user").value(loan.idUser))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id_book").value(loan.idBook))
    }

    @Test
    fun `Quando pegar o livro, então deve retornar 200ok`() {
        val loan = Loan(
            id = "1",
            idBook = "book2",
            idUser = "user2",
            status = LoanStatus.REQUESTED_LOAN,
            expectedReturnDate = LocalDate.of(2024, 10, 28),
            userReturnDate = LocalDate.of(2024, 10, 30)
        )
        every { loanService.grabLoanBook("1") } returns loan

        mockMvc.perform(
            put("/grab/{idLoan}", "1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(loan.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id_user").value(loan.idUser))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id_book").value(loan.idBook))
    }

    @Test
    fun `Quando devolver o livro, então deve retornar 200ok`() {
        val loan = Loan(
            id = "1",
            idBook = "book2",
            idUser = "user2",
            status = LoanStatus.REQUESTED_LOAN,
            expectedReturnDate = LocalDate.of(2024, 10, 28),
            userReturnDate = LocalDate.of(2024, 10, 30)
        )
        every { loanService.bookReturn("1") } returns loan

        mockMvc.perform(
            put("/devolution/{idLoan}", "1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(loan.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id_user").value(loan.idUser))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id_book").value(loan.idBook))
    }
}