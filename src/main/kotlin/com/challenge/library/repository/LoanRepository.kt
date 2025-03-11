package com.challenge.library.repository

import com.challenge.library.controller.dto.ThreeMostBorrowedBooksDTO
import com.challenge.library.model.Loan
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface LoanRepository: MongoRepository<Loan, String> {
    fun findAllByUserId(userId: String, pagination: Pageable): Page<Loan>
    fun findAllByBookId(bookId: String?): List<Loan>

    @Query("{'status': 'REQUESTED_LOAN'}")
    fun findLoans(pagination: Pageable): Page<Loan>

    @Query("{'loanDate': { \$gte: ?0, \$lte: ?1 } }")
    fun findLoansByPeriod(start: LocalDateTime, end: LocalDateTime): List<Loan>

    @Aggregation(
        "{ \$group: { _id: '\$idBook', totalLoans: { \$sum: 1 } } }",
        "{ \$sort: { totalLoans: -1 } }",
        "{ \$limit: 3 }",
        "{ \$project: { idBook : '\$_id', totalLoans: \$totalLoans } }"
    )
    fun threeMostBorrowedBooks(): List<ThreeMostBorrowedBooksDTO>

    @Query("{'status': 'RETURNED'}")
    fun findAllLoansReturns(): List<Loan>
}