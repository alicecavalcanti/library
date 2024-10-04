package com.challenge.library.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document


@Document(value = "loan_and_return")
class LoanAndReturn(
    @Id
    val id: String?= null,
    val idUser: String,
    val idBook: String,
    var status: LoanStatus
){

}