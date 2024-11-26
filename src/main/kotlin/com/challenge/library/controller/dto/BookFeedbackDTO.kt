package com.challenge.library.controller.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

class BookFeedbackDTO (
    val idBook: String,
    @field:Size(min=10, max= 200)
    val resenha: String,

    @field:Min(0)
    @field:Max(5)
    val nota: Int
)