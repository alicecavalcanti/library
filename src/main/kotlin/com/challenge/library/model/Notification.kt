package com.challenge.library.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "notification")
data class Notification (
    @Id
    val id: String?=null,
    val idUser: String,
    val mensagem: String
)
