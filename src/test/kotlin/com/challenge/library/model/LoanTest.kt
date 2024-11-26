package com.challenge.library.model


import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LoanTest() {

    @Test
    fun `Quando o status do empréstimo não é do tipo devolvido, então deve retornar verdadeiro`(){
        val loan = Loan(
            id = "1",
            idBook = "2",
            idUser = "2",
            status = LoanStatus.REQUESTED_LOAN
        )
        val result = loan.isLoanNotReturned()
        Assertions.assertTrue(result)
    }
    @Test
    fun `Quando o status é do tipo devolvido, então deve retornar falso`(){
        val loan = Loan(
            id = "1",
            idBook = "2",
            idUser = "2",
            status = LoanStatus.RETURNED
        )
        val result = loan.isLoanNotReturned()
        Assertions.assertFalse(result)
    }

    @Test
    fun `Quando fizer a ação de aprovar o emprétimo, então o empréstimo deve ser aprovado e o status deve mudar para APROVADO`(){
        var loan = Loan(
            id = "1",
            idBook = "2",
            idUser = "2",
            status = LoanStatus.REQUESTED_LOAN
        )
        val result = loan.approveLoan()
        Assertions.assertTrue(result)
        Assertions.assertEquals(loan.status, LoanStatus.APPROVED)
    }

    @Test
    fun `Quando e fizer a ação de aprovar o emprétimo, então o empréstimo não deve ser feito e o status deve permanecer o mesmo`(){
        var loan = Loan(
            id = "1",
            idBook = "2",
            idUser = "2",
            status = LoanStatus.RETURNED
        )
        val result = loan.approveLoan()
        Assertions.assertFalse(result)
        Assertions.assertEquals(loan.status, LoanStatus.RETURNED)
    }

    @Test
    fun `Quando fizer a ação de aprovar a devolução, então a devolução deve ser aprovada e o status deve mudar para RETURNED`(){
        var loan = Loan(
            id = "1",
            idBook = "2",
            idUser = "2",
            status = LoanStatus.REQUESTED_RETURN
        )
        val result = loan.approveReturn()
        Assertions.assertTrue(result)
        Assertions.assertEquals(loan.status, LoanStatus.RETURNED)
    }

    @Test
    fun `Quando e fizer a ação de aprovar a devolução, então a devolução não deve ser feita e o status permanece o mesmo`(){
        var loan = Loan(
            id = "1",
            idBook = "2",
            idUser = "2",
            status = LoanStatus.REQUESTED_LOAN
        )
        val result = loan.approveReturn()
        Assertions.assertFalse(result)
        Assertions.assertEquals(loan.status, LoanStatus.REQUESTED_LOAN)
    }

    @Test
    fun `Quando fizer a ação de pegar um livro, então o status deve mudar para CHECKED_OUT `(){
        var loan = Loan(
            id = "1",
            idBook = "2",
            idUser = "2",
            status = LoanStatus.APPROVED
        )
        val result = loan.grabBook()
        Assertions.assertTrue(result)
        Assertions.assertEquals(loan.status, LoanStatus.CHECKED_OUT)

    }
    @Test
    fun `Quando fizer a ação de pegar um livro, então a ação não pode ser feita e o status permanece o mesmo`(){
        val loan = Loan(
            id = "1",
            idBook = "2",
            idUser = "2",
            status = LoanStatus.REQUESTED_LOAN
        )
        val result = loan.grabBook()
        Assertions.assertFalse(result)
        Assertions.assertEquals(loan.status, LoanStatus.REQUESTED_LOAN)
    }

    @Test
    fun `Quando solicitar o retorno do livro, então o status deve mudar para REQUESTED_RETURN e a solicitação ser feita`(){
        val loan = Loan(
            id = "1",
            idBook = "2",
            idUser = "2",
            status = LoanStatus.CHECKED_OUT
        )

        val result = loan.requestbookReturn()
        Assertions.assertTrue(result)
        Assertions.assertEquals(loan.status, LoanStatus.REQUESTED_RETURN)
    }

    @Test
    fun `Quando solicitar o retorno do livro, então a solicitação não deve ser feita o status deve permanecer o mesmo`(){
        val loan = Loan(
            id = "1",
            idBook = "2",
            idUser = "2",
            status = LoanStatus.APPROVED
        )

        val result = loan.requestbookReturn()
        Assertions.assertFalse(result)
        Assertions.assertEquals(loan.status, LoanStatus.APPROVED)
    }

    @Test
    fun `Quando verificar se a devolução está no prazo, então ela precisa está dentro do prazo `(){
        val loan = Loan(
            id = "1",
            idBook = "2",
            idUser = "2",
            status = LoanStatus.APPROVED,
            expectedReturnDate = LocalDate.of(2024,10,28),
            userReturnDate = LocalDate.of(2024,10,12)
        )

        val result = loan.isReturnOnTime(loan.userReturnDate!!)
        Assertions.assertTrue(result)
    }

    @Test
    fun `Quando verificar se a devolução está no prazo, então ela deve tá atrasada`(){
        val loan = Loan(
            id = "1",
            idBook = "2",
            idUser = "2",
            status = LoanStatus.APPROVED,
            expectedReturnDate = LocalDate.of(2024,10,28),
            userReturnDate = LocalDate.of(2024,10,30)
        )

        val result = loan.isReturnOnTime(loan.userReturnDate!!)
        Assertions.assertFalse(result)
    }
}