package com.challenge.library.service

import com.challenge.library.controller.dto.LoanBookDTO
import com.challenge.library.mapper.LoanRequestMapper
import com.challenge.library.repository.LoanRepository
import io.mockk.mockk
import org.junit.jupiter.api.Test

class LoanServiceTest {
    private val loanRepository: LoanRepository = mockk()
    private val bookService: BookService = mockk()
    private val userService: UserService= mockk()
    private val loanRequestMapper: LoanRequestMapper = mockk()
    val loanService = LoanService(
        loanRepository, bookService, userService, loanRequestMapper
    )

    @Test
    fun `deve retornar uma exception de status diferente do esperado`(){
        val loanBookDTO: LoanBookDTO = mockk()
        loanService.registerLoad(loanBookDTO)

    }

}