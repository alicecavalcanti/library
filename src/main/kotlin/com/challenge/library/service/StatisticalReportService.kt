package com.challenge.library.service

import com.challenge.library.model.StatisticalReport
import com.challenge.library.repository.StatisticalReportRepository
import org.springframework.stereotype.Service


@Service
class StatisticalReportService(
    private val loanService: LoanService,
    private val userService: UserService,
    private val bookService: BookService,
    private val statisticalReportRepository: StatisticalReportRepository
) {

    fun statisticalReport(): StatisticalReport {
        val statisticalReport = StatisticalReport(
            totalLoansMonth= loanService.findAllLoanMonth(),
            totalNumberQuantityEachTypeUsers= userService.totalNumberUsers(),
            lateAndOnTimeReturns = loanService.lateAndOnTimeReturns(),
            fiveMostBorrowedBooks = loanService.fiveMostBorrowedBooks(),
            bestBookNotes = bookService.bestBookNotes(),
            newUsersLastMonth= userService.newUsersLastMonth()
        )
        return statisticalReportRepository.save(statisticalReport)
    }
}