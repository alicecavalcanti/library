package com.challenge.library.model

import com.challenge.library.controller.dto.*
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(value = "statistical-report")
data class StatisticalReport (
    @Id
    val id: String?=null,
    val totalLoansMonth: List<Loan>?,
    var totalNumberQuantityEachTypeUsers: QuantityEachTypeUsers?,
    val lateAndOnTimeReturns: List<LateAndOnTimeReturnsDTO>?,
    val fiveMostBorrowedBooks: List<ThreeMostBorrowedBooksDTO>?,
    val bestBookNotes: List<AverageBookGradesDTO>?,
    val newUsersLastMonth:UserGrowthDTO?

) {
    constructor(
        totalLoansMonth: List<Loan>,
        lateAndOnTimeReturns: List<LateAndOnTimeReturnsDTO>,
        fiveMostBorrowedBooks: List<ThreeMostBorrowedBooksDTO>
    ) : this(
        null,
        totalLoansMonth,
        null,
        lateAndOnTimeReturns,
        fiveMostBorrowedBooks,
        null,
        null
    )

    constructor(
        totalNumberQuantityEachTypeUsers: QuantityEachTypeUsers,
        newUsersLastMonth: UserGrowthDTO
    ) : this(
        null,
        null,
        totalNumberQuantityEachTypeUsers,
        null,
        null,
        null,
        newUsersLastMonth
    )

    constructor(
        bestBookNotes: List<AverageBookGradesDTO>
    ) : this(
        null,
        null,
        null,
        null,
        null,
        bestBookNotes,
        null
    )

}




