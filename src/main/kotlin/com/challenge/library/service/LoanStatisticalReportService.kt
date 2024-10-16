package com.challenge.library.service

import com.challenge.library.controller.dto.FiveMostBorrowedBooksDTO
import com.challenge.library.controller.dto.LateAndOnTimeReturnsDTO
import com.challenge.library.model.Loan
import com.challenge.library.repository.LoanRepository
import java.time.LocalDate

class LoanStatisticalReportService(
   private val loanRepository: LoanRepository
) {
    fun findAllLoanMonth(): List<Loan>{
        val lastMonth= LocalDate.now().minusMonths(1)
        return loanRepository.findAllLoanMonth(lastMonth.monthValue)
    }

    fun fiveMostBorrowedBooks(): List<FiveMostBorrowedBooksDTO>{
        return loanRepository.fiveMostBorrowedBooks()
    }

    fun LateAndOnTimeReturns(): List<LateAndOnTimeReturnsDTO>{
        return loanRepository.findLateAndOnTimeReturns()
    }


}