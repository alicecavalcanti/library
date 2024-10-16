package com.challenge.library.model

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document(collection = "user")
data class User(
    @Id
    val id: String?=null,
    val username: String,
    val password: String,
    val registrationDate: LocalDate?=null,
    val roles: List<Roles>
)