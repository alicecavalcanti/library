package com.challenge.library.controller.dto

import com.challenge.library.model.Roles
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignUpRequestDTO (
    @field:NotBlank
    @Size(min = 5, max = 256)
    val username: String,
    val name: String,
    @field:NotBlank
    @Size(min = 5, max = 100)
    val password: String,
    val role: List<Roles>
)