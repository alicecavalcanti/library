package com.challenge.library.data
import com.challenge.library.controller.dto.LoanBookDTO
import java.time.LocalDate

object LoanBookDTOData {
    fun build() = LoanBookDTO(
        idBook = "1",
        idUser = "5",
        expectedReturnDate = LocalDate.of(2024, 10, 28)
    )
}