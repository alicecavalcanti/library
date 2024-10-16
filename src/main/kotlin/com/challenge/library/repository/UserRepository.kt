package com.challenge.library.repository

import com.challenge.library.model.Roles
import com.challenge.library.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : MongoRepository<User, String>{

    fun findByRoles(roles: List<Roles>): List<User>
    @Query("{ \$expr: { \$eq: [ { \$month: '\$registrationDate' }, ?0 ] } }")
    fun findAllLoanDate(month: Int): List<User>

}