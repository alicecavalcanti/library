package com.challenge.library.controller.dto

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

class BookUpdateRequestDTO (
    @field:NotBlank
    @Size(max = 200)
    val id: String,

    @field:NotBlank
    @Size(min=1, max = 100)
    val titulo: String,

    @field:NotBlank
    @field:Size(min=50, max = 700)
    val resumo: String,

    @field:NotBlank
    @field:Size(min=5, max=30)
    val autor:String,

    @NotBlank
    @field:Size(min=13, max= 13 )
    val ISBN: String,

    @field:NotEmpty
    @field:Size(min = 1, max = 100)
    val categoria: List<String>
)