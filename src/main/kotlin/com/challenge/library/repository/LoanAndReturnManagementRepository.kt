package com.challenge.library.repository

import com.challenge.library.model.LoanAndReturn
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface LoanAndReturnManagementRepository: MongoRepository<LoanAndReturn, String> {
    fun findAllByIdUser(idUser: String, pagination: Pageable): Page<LoanAndReturn>
    fun findAllByIdBook(id: String?): List<LoanAndReturn>


}