package com.challenge.library.data

import com.challenge.library.model.Book
import com.challenge.library.model.Loan
import com.challenge.library.model.LoanStatus
import java.time.LocalDate

object ListLoanData {
    fun build() = listOf(
        Loan(
            id = "1",
            idUser = "user1",
            idBook = "book1",
            status = LoanStatus.REQUESTED_LOAN,
            loanDate = LocalDate.of(2024,5,10),
            expectedReturnDate = LocalDate.of(2024, 11, 10),
            userReturnDate = LocalDate.of(2024, 10, 18)
        ),
        Loan(
            id = "2",
            idUser = "user1",
            idBook = "book1",
            status = LoanStatus.REQUESTED_LOAN,
            loanDate = LocalDate.of(2024,5,20),
            expectedReturnDate = LocalDate.of(2024, 10, 20),
            userReturnDate = LocalDate.of(2024, 10, 18)
        ),
        Loan(
            id = "3",
            idUser = "user1",
            idBook = "book1",
            status = LoanStatus.CHECKED_OUT,
            loanDate = LocalDate.of(2024,4,15),
            expectedReturnDate = LocalDate.of(2024, 11, 15),
            userReturnDate = null
        ),
        Loan(
            id = "4",
            idUser = "user1",
            idBook = "book2",
            status = LoanStatus.RETURNED,
            loanDate = LocalDate.of(2024,6,1),
            expectedReturnDate = LocalDate.of(2024, 10, 25),
            userReturnDate = null
        ),
        Loan(
            id = "5",
            idUser = "user1",
            idBook = "book2",
            status = LoanStatus.REQUESTED_RETURN,
            loanDate = LocalDate.of(2024,5,28),
            expectedReturnDate = LocalDate.of(2024, 11, 10),
            userReturnDate = LocalDate.of(2024, 10, 18)
        ),
        Loan(
            id = "6",
            idUser = "user1",
            idBook = "book3",
            status = LoanStatus.APPROVED,
            loanDate = LocalDate.of(2024,5,12),
            expectedReturnDate = LocalDate.of(2024, 11, 10),
            userReturnDate = LocalDate.of(2024, 10, 18)
        )
    )
}