package com.challenge.library.controller

import com.challenge.library.model.StatisticalReport
import com.challenge.library.repository.StatisticalReportRepository
import com.challenge.library.service.BookService
import com.challenge.library.service.LoanService
import com.challenge.library.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/statistical-report")
class StatisticalReportController(
    private val loanService: LoanService,
    private val userService: UserService,
    private val bookService: BookService,
    private val statisticalReportRepository: StatisticalReportRepository
) {
    @GetMapping("/loan")
    fun generateLoanStatistics(): StatisticalReport {
        val loanStatistics = StatisticalReport(
            totalLoansMonth = loanService.findAllLoanMonth(),
            lateAndOnTimeReturns = loanService.lateAndOnTimeReturns(),
            fiveMostBorrowedBooks = loanService.fiveMostBorrowedBooks()
        )
        return statisticalReportRepository.save(loanStatistics)
    }

    @GetMapping("/user")
    fun generateUserStatistics(): StatisticalReport {
        val userStatistics = StatisticalReport(
            totalNumberQuantityEachTypeUsers = userService.totalNumberUsers(),
            newUsersLastMonth = userService.newUsersLastMonth()
        )
        return statisticalReportRepository.save(userStatistics)
    }

    @GetMapping("/book")
    fun generateBookStatistics(): StatisticalReport {
        val bookStatistics = StatisticalReport(
            bestBookNotes = bookService.bestBookNotes()
        )
        return statisticalReportRepository.save(bookStatistics)
    }
}