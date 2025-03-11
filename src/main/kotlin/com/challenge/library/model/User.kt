package com.challenge.library.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document(collection = "user")
data class User(
    @Id
    val id: String?=null,
    val name: String,
    val username: String,
    var password: String,
    val registrationDate: LocalDate?=null,
    val roles: List<Roles>
)