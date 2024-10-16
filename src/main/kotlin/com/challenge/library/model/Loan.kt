package com.challenge.library.model

import com.challenge.library.exception.*
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document(value = "loan")
class Loan(
    @Id
    val id: String?= null,
    val idUser: String,
    val idBook: String,
    var status: LoanStatus,
    var expectedReturnDate: LocalDate ?= null,
    var userReturnDate:LocalDate?=null
){

    fun checkIfStatusNotValid(expectedStatus: LoanStatus){
        if(this.status != expectedStatus){
            if(expectedStatus == LoanStatus.RETURNED) throw BookRequestedException()
            if(expectedStatus == LoanStatus.REQUESTED_LOAN) throw StatusRequestedLoanException()
            if(expectedStatus == LoanStatus.REQUESTED_RETURN) throw StatusRequestedReturnException()
            if(expectedStatus ==  LoanStatus.APPROVED) throw StatusApprovedException()
            if(expectedStatus == LoanStatus.CHECKED_OUT) throw StatusCheckedOutException()
        }

    }

}