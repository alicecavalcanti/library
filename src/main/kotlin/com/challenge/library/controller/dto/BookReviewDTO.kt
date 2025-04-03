package com.challenge.library.controller.dto

import jakarta.validation.constraints.Size

data class BookReviewDTO (
    val idBook: String,

    @field:Size(min=10, max= 200)
    val review: String,
)