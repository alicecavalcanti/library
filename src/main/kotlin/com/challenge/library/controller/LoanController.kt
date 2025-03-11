package com.challenge.library.controller

import com.challenge.library.controller.dto.*
import com.challenge.library.model.Loan
import com.challenge.library.model.StatisticalReport
import com.challenge.library.service.LoanService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
class LoanController(
    private val loanService: LoanService,
){
    @GetMapping("/list")
    fun listAllLibraryLoans(
        pagination: Pageable
    ): Page<Loan>{
        return loanService.getAllLoans(pagination)
    }

    @GetMapping("/list/{idUser}")
    fun listUserBookLoan(
        @PathVariable idUser: String,
        pagination: Pageable
    ): Page<Loan>?{
        return loanService.getMyBookLoans(user.id, pagination)
    }

    @PostMapping("/request")
    fun requestLoan(
        @RequestBody loan: LoanBookDTO,
        uriComponent: UriComponentsBuilder
    ): ResponseEntity<Loan>{
        val createLoan= loanService.requestLoan(loan, user.id)
        val uri = uriComponent.path("/register").build().toUri()
        return ResponseEntity.created(uri).body(createLoan)
    }

    @PutMapping("/approve/{idLoan}")
    fun approveLoanRequest(
       @PathVariable idLoan: String
    ): ResponseEntity<Loan>{
        return ResponseEntity
                    .status(200)
                    .body(loanService.approveLoanRequest(loanId))
    }

    @PutMapping("/approve-return/{idLoan}")
    fun approveReturnRequest(
       @PathVariable idLoan: String
    ): Loan{
        return loanService.approveReturnRequest(loanId)
    }


    @PutMapping("/grab/{idLoan}")
    fun grabBook(
        @PathVariable loanId: String,
    ): ResponseEntity<Loan>{
        return ResponseEntity
            .status(200)
            .body(loanService.grabLoanBook(loanId))
    }

    @PutMapping("/devolution/{idLoan}")
    fun bookReturn(
        @PathVariable loanId: String
    ): ResponseEntity<Loan>{
        return ResponseEntity
            .status(200)
            .body(loanService.bookReturn(loanId))
    }
}