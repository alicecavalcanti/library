package com.challenge.library.repository

import com.challenge.library.controller.dto.FiveMostBorrowedBooksDTO
import com.challenge.library.controller.dto.LateAndOnTimeReturnsDTO
import com.challenge.library.model.Loan
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface LoanRepository: MongoRepository<Loan, String> {
    fun findAllByIdUser(idUser: String, pagination: Pageable): Page<Loan>
    fun findAllByIdBook(id: String?): List<Loan>

    @Query("{'status': 'REQUESTED_LOAN'}")
    fun findLoans(pagination: Pageable): Page<Loan>

    @Query("{ \$expr: { \$eq: [ { \$month: '\$loanDate' }, ?0 ] } }")
    fun findAllLoanMonth(month: Int): List<Loan>

    @Aggregation(
        "{ \$group: { _id: '\$idBook', totalLoans: { \$sum: 1 } } }",
        "{ \$sort: { totalLoans: -1 } }",
        "{ \$limit: 5 }",
        "{ \$project: { idBook : '\$_id', totalLoans: \$totalLoans } }"
    )
    fun fiveMostBorrowedBooks(): List<FiveMostBorrowedBooksDTO>


    @Aggregation(
        "{\$match: {status: 'RETURNED' }}",
        "{\$addFields: {returns: {\$lte: ['\$userReturnDate', '\$expectedReturnDate'] }}}",
        "{\$group: {" +

                "_id: '\$idBook' ," +

                "loanOnTime: {\$sum: {\$cond: {" +
                "if: {\$eq: ['\$returns', true]}, then:1, else:0 " +
                "}}}," +

                "lateLoan: {\$sum: { \$cond: {" +
                "if: {\$eq: ['\$returns', false]}, then:1, else:0" +
                "}}}" +
        "}}",
        "{\$project: {idBook: '\$_id', loanOnTime: '\$loanOnTime', lateLoan: '\$lateLoan' }}"
    )
    fun findLateAndOnTimeReturns(): List<LateAndOnTimeReturnsDTO>


}