package com.challenge.library.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

class BookRequestDTO (
    @field:NotBlank
    @field:Size(min=1, max = 100)
    val titulo: String,

    @field:NotBlank
    @field:Size(min=50, max = 700)
    val resumo: String,

    @field:NotBlank
    @field:Size(min=5, max=30)
    val autor:String,

    @field:NotBlank
    @field:Size(min=13, max= 13 )
    val isbn: String,

    @field:NotEmpty
    @field:Size(min = 2, max = 100)
    val categoria: List<String>,


)