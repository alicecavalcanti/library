package com.challenge.library

import com.challenge.library.controller.dto.LoanBookDTO
import com.challenge.library.data.ListBooksData
import com.challenge.library.data.ListLoanData
import com.challenge.library.model.LoanStatus
import com.challenge.library.model.Roles
import com.challenge.library.model.User
import com.challenge.library.repository.BookRepository
import com.challenge.library.repository.LoanRepository
import com.challenge.library.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder
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
import java.time.LocalDate

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class LoanIntegrationTest {

    companion object {
        @JvmStatic
        @Container
        @ServiceConnection
        var mongoDBContainer = MongoDBContainer("mongo:latest")
            .withExposedPorts(27017)
    }

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var loanRepository: LoanRepository

    @Autowired
    private lateinit var bookRepository : BookRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    val listBookdata = ListBooksData.build()

    val listLoanData = ListLoanData.build()

    @BeforeEach
    fun setup(){
        loanRepository.deleteAll()
        userRepository.deleteAll()
        bookRepository.deleteAll()
    }

    @Test
    fun `When I try to get all loans, Then it should list`(){
        val loan1= listLoanData[0]
        val loan2= listLoanData[1]

        val  loans = listOf(loan1, loan2)

        loanRepository.saveAll(loans)

        mockMvc.perform(
            get("/list")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.total_elements").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].id").value(containsInAnyOrder(loan1.id, loan2.id)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].id_user").value(containsInAnyOrder(loan1.idUser, loan2.idUser)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].id_book").value(containsInAnyOrder(loan1.idBook, loan2.idBook)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].status").value(containsInAnyOrder(loan1.status.name, loan2.status.name)))
    }

    @Test
    fun `When I try to get all the user's loans, Then it should list`(){
        val  loan1 = listLoanData[0]
        val  loan2 = listLoanData[1]

        val user = User(
            id = "user1",
            username = "user@gmail.com",
            password = "user123",
            roles = listOf(Roles.ADMIN)
        )

        val loans = listOf(loan2, loan1)

        loanRepository.saveAll(loans)

        userRepository.save(user)

        mockMvc.perform(
            get("/list/{idUser}", "user1")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.total_elements").value(loans.size))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].id").value(containsInAnyOrder(loan1.id,loan2.id)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].id_user").value(containsInAnyOrder(loan1.idUser, loan2.idUser)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].id_book").value(containsInAnyOrder(loan1.idBook, loan2.idBook)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].status").value(containsInAnyOrder(loan1.status.name, loan2.status.name)))
    }

    @Test
    fun `When a loan is registered, Then it should be returned`(){
        bookRepository.save(listBookdata[0])

        val loanBookDTO = LoanBookDTO(
            idBook = "1",
            idUser = "user1",
            expectedReturnDate = LocalDate.of(2024, 11, 10),
        )

        mockMvc.perform(
            post("/register")
                .content(objectMapper.writeValueAsString(loanBookDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id_user").value("user1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id_book").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(LoanStatus.REQUESTED_LOAN.name))
    }

    @Test
    fun `Quando aprovar um empréstimo, então deve retornar o empréstimo aprovado e mudar o status para approve`(){
        val  loan = listLoanData[0]

        loanRepository.save(loan)

        mockMvc.perform(
            put("/approve/{idLoan}", loan.id)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(loan.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id_user").value(loan.idUser))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id_book").value(loan.idBook))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(LoanStatus.APPROVED.name))
    }

    @Test
    fun `Quando aprovar um retorno de empréstimo, então deve mudar o status para RETURNED`(){
        val  loan = listLoanData[4]

        loanRepository.save(loan)
        mockMvc.perform(
            put("/approve-return/{idLoan}", loan.id)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(5))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id_user").value("user1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id_book").value("book2"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(LoanStatus.RETURNED.name))
    }

    @Test
    fun `Quando pegar um livro, então deve mudar o status e ele ser pego`(){
        val  loan = listLoanData[5]

        loanRepository.save(loan)
        mockMvc.perform(
            put("/grab/{idLoan}", loan.id)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(6))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id_user").value("user1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id_book").value("book3"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(LoanStatus.CHECKED_OUT.name))
    }

    @Test
    fun `Quando devolver um livro, então deve mudar o status para requestedReturn`(){
        val  loan = listLoanData[2]
        loanRepository.save(loan)

        mockMvc.perform(
            put("/devolution/{idLoan}", loan.id)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(loan.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id_user").value(loan.idUser))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id_book").value(loan.idBook))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(LoanStatus.REQUESTED_RETURN.name))
    }
}

