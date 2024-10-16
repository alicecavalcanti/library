package com.challenge.library.controller.dto

import java.time.LocalDate

data class LoanBookDTO (
    val idUser: String,
    val idBook: String,
    val expectedReturnDate: LocalDate

)