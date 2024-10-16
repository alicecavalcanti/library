package com.challenge.library.model

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Page
import org.springframework.data.mongodb.core.mapping.Document
import javax.swing.border.TitledBorder

@Document(collection = "notification")
data class Notification (
    @Id
    val id: String?=null,
    val idUser: String,
    val mensagem: String
)