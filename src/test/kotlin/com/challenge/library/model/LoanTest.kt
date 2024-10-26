package com.challenge.library.model


import com.challenge.library.exception.StatusRequestedLoanException
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class LoanTest() {

    var loan: Loan = Loan(
        id = "1",
        idBook = "2",
        idUser = "2",
        status = LoanStatus.RETURNED
    )

    @Test
    fun `deve retornar 200OK ao passar o mesmo valor para status e expectedStatus`(){
        loan.checkIfStatusNotValid(LoanStatus.RETURNED)

        Assertions.assertEquals(loan.status, loan.checkIfStatusNotValid(LoanStatus.REQUESTED_LOAN))
    }

}