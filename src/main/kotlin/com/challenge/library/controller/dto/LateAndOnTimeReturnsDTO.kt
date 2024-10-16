package com.challenge.library.controller.dto

data class LateAndOnTimeReturnsDTO (
    val idBook: String,
    val loanOnTime: Int,
    val lateLoan:Int
)
