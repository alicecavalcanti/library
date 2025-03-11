package com.challenge.library.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document(value = "loan")
class Loan(
    @Id
    var id: String?= null,
    val userId: String,
    val bookId: String,
    var status: LoanStatus = LoanStatus.CHECKED_OUT,
    var loanDate: LocalDate?=null,
    var expectedReturnDate: LocalDate ?= null,
    var userReturnDate:LocalDate?=null
)  {
   fun isLoanNotReturned(): Boolean {
        return this.status != LoanStatus.RETURNED
    }

    fun approveLoan(): Boolean{
        if (this.status != LoanStatus.REQUESTED_LOAN) return false
        this.status = LoanStatus.APPROVED
        return true
    }

    fun approveReturn(): Boolean{
        if(this.status != LoanStatus.REQUESTED_RETURN) return false
        this.status= LoanStatus.RETURNED
        return true
    }

    fun grabBook(): Boolean{
        if(this.status != LoanStatus.APPROVED) return false
        this.status = LoanStatus.CHECKED_OUT
        return true
    }

    fun requestBookReturn(): Boolean{
        if (this.status != LoanStatus.CHECKED_OUT) return false
        this.status = LoanStatus.REQUESTED_RETURN
        this.userReturnDate = LocalDate.now()
        return true
    }

    fun isReturnOnTime(userReturnDate: LocalDate): Boolean{
        return userReturnDate.isBefore(this.expectedReturnDate) || userReturnDate.isEqual(this.expectedReturnDate)
    }
}