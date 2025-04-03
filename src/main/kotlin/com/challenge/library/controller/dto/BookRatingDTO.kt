package com.challenge.library.controller.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class BookRatingDTO(
    val idBook: String,

    @field:Min(0)
    @field:Max(5)
    val rating: Int
)
