package com.challenge.library.repository

import com.challenge.library.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String>{

    fun findByUsernameAndPassword(username: String, password: String): User
}