package com.challenge.library.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document(collection= "Books")
data class Books(
    @Id
    @JsonSerialize(using = ToStringSerializer::class)
    var id: ObjectId,
    var titulo: String,
    var resumo: String,
    var autor:String,
    var ISBN: String,
    var categoria: List<String>,
    val notas: Double?= null,
    val resenhas: String?=null

) : Serializable