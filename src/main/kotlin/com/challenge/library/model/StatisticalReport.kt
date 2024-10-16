package com.challenge.library.model

import com.challenge.library.controller.dto.*
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(value = "statistical-report")
class StatisticalReport (
    @Id
    val id: String?=null,
    val totalLoansMonth: List<Loan>,
    var totalNumberQuantityEachTypeUsers: QuantityEachTypeUsers,
    val LateAndOnTimeReturns: List<LateAndOnTimeReturnsDTO>,
    val fiveMostBorrowedBooks: List<FiveMostBorrowedBooksDTO>,
    val amountLoansCategory: List<LoanAmountCategoryDTO>,
    val bestBookNotes: List<AverageBookGradesDTO>,
    val newUsersLastMonth:UserGrowthDTO
)

