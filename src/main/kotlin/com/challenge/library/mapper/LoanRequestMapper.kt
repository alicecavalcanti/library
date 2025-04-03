package com.challenge.library.mapper

import com.challenge.library.controller.dto.LoanBookDTO
import com.challenge.library.model.Loan
import com.challenge.library.model.LoanStatus
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class LoanRequestMapper : Mapper<LoanBookDTO, Loan>{
    override fun map(t: LoanBookDTO): Loan {
        return Loan(
            bookId = t.bookId,
            userId = t.userId!!,
            status = LoanStatus.REQUESTED_LOAN,
            loanDate = LocalDate.now(),
            expectedReturnDate = t.expectedReturnDate
        )
    }
}