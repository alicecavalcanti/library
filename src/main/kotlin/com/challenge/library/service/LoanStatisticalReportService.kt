package com.challenge.library.service

import com.challenge.library.controller.dto.FiveMostBorrowedBooksDTO
import com.challenge.library.controller.dto.LateAndOnTimeReturnsDTO
import com.challenge.library.model.Loan
import com.challenge.library.repository.LoanRepository
import java.time.LocalDate
import java.time.LocalTime

class LoanStatisticalReportService(
   private val loanRepository: LoanRepository
) {
    fun findAllLoanMonth(): List<Loan>{
        val end = LocalDate.now().atTime(LocalTime.MAX)
        val start = LocalDate.now().atStartOfDay().minusMonths(1)
        return loanRepository.findLoansByPeriod(start, end)
    }

    fun fiveMostBorrowedBooks(): List<FiveMostBorrowedBooksDTO>{
        return loanRepository.fiveMostBorrowedBooks()
    }

    fun LateAndOnTimeReturns(): List<LateAndOnTimeReturnsDTO>{
        return loanRepository.findLateAndOnTimeReturns()
    }


}