package com.challenge.library.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserRequestDTO (
    @field:NotBlank
    @Size(min = 5, max = 256)
    val username: String,

    @field:NotBlank
    @Size(min = 5, max = 100)
    val password: String
)