package com.challenge.library.controller.dto

import java.time.LocalDate

data class LoanBookDTO (
    var userId: String? = null,
    val bookId: String,
    val expectedReturnDate: LocalDate
)