package com.challenge.library.controller.dto

import com.challenge.library.model.Loan

data class LoanStatisticsDTO(
    val totalLoansMonth: List<Loan>,
    val lateAndOnTimeReturns: List<LateAndOnTimeReturnsDTO>,
    val fiveMostBorrowedBooks: List<ThreeMostBorrowedBooksDTO>,
)