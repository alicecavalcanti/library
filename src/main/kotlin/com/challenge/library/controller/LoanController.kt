package com.challenge.library.controller

import com.challenge.library.controller.dto.*
import com.challenge.library.model.Loan
import com.challenge.library.model.StatisticalReport
import com.challenge.library.service.LoanService
import com.challenge.library.service.StatisticalReportService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
class LoanController(
    private val loanService: LoanService,
    private val statisticalReportService: StatisticalReportService

){
    @GetMapping("/list")
    fun listAllLibraryLoans(
        pagination: Pageable
    ): Page<Loan>{
        return loanService.listAllLibraryLoans(pagination)
    }

    @GetMapping("/list/{idUser}")
    fun ListUserBookLoan(
        @PathVariable idUser: String,
        pagination: Pageable
    ): Page<Loan>?{
        return loanService.listUserBookLoan(idUser, pagination)
    }


    @PostMapping("/register")
    fun registerLoad(
        @RequestBody loan: LoanBookDTO,
        uriComponent: UriComponentsBuilder
    ): ResponseEntity<Loan>{

        val createLoan= loanService.registerLoad(loan)

        val uri = uriComponent.path("/loan").build().toUri()

        return ResponseEntity.created(uri).body(createLoan)
    }

    @PutMapping("/approve/{idLoan}")
    fun approveLoanRequest(
       @PathVariable idLoan: String
    ): ResponseEntity<Loan>{
        return ResponseEntity
                    .status(200)
                    .body(loanService.approveLoanRequest(idLoan))
    }

    @PutMapping("/approve-return/{idLoan}")
    fun approveReturnRequest(
       @PathVariable idLoan: String
    ): Loan{
        return loanService.approveReturnRequest(idLoan)
    }


    @PutMapping("/grab/{idLoan}")
    fun grabBook(
        @PathVariable idLoan: String
    ): ResponseEntity<Loan>{
        return ResponseEntity
            .status(200)
            .body(loanService.grabBook(idLoan))

    }

    @PutMapping("/devolution/{idLoan}")
    fun bookReturn(
        @PathVariable idLoan: String
    ): ResponseEntity<Loan>{
        return ResponseEntity
            .status(200)
            .body(loanService.bookReturn(idLoan))
    }

    @GetMapping("/report")
    fun statisticalReport(): StatisticalReport{
        return statisticalReportService.statisticalReport()
    }
}