package com.challenge.library.controller

import com.challenge.library.configuration.security.UserAuthenticationPrincipal
import com.challenge.library.controller.dto.*
import com.challenge.library.model.Loan
import com.challenge.library.model.project.ResourcePermission
import com.challenge.library.service.LoanService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/loan")
class LoanController(
    private val loanService: LoanService,
){
    @GetMapping("/list")
    @PreAuthorize("hasPermission(#user.id, '${ResourcePermission.RESOURCE_LOAN}', '${ResourcePermission.ACTION_READ}')")
    fun getAllLoans(
        pagination: Pageable,
        @AuthenticationPrincipal user: UserAuthenticationPrincipal
    ): Page<Loan>{
        return loanService.getAllLoans(pagination)
    }

    @GetMapping("/my-loans")
    fun getMyBookLoans(
        @AuthenticationPrincipal user: UserAuthenticationPrincipal,
        pagination: Pageable
    ): Page<Loan>?{
        return loanService.getMyBookLoans(user.id, pagination)
    }

    @PostMapping("/request")
    fun requestLoan(
        @RequestBody loan: LoanBookDTO,
        @AuthenticationPrincipal user: UserAuthenticationPrincipal,
        uriComponent: UriComponentsBuilder
    ): ResponseEntity<Loan>{
        val createLoan= loanService.requestLoan(loan, user.id)
        val uri = uriComponent.path("/register").build().toUri()
        return ResponseEntity.created(uri).body(createLoan)
    }

    @PutMapping("/approve/{loanId}")
    @PreAuthorize("hasPermission(#user.id, '${ResourcePermission.RESOURCE_LOAN}', '${ResourcePermission.ACTION_APPROVE_LOAN}')")
    fun approveLoanRequest(
        @PathVariable loanId: String,
        @AuthenticationPrincipal user: UserAuthenticationPrincipal
    ): ResponseEntity<Loan>{
        return ResponseEntity
                    .status(200)
                    .body(loanService.approveLoanRequest(loanId))
    }

    @PutMapping("/approve-return/{loanId}")
    @PreAuthorize("hasPermission(#user.id, '${ResourcePermission.RESOURCE_LOAN}', '${ResourcePermission.ACTION_APPROVE_RETURN}')")
    fun approveReturnRequest(
       @PathVariable loanId: String,
       @AuthenticationPrincipal user: UserAuthenticationPrincipal
    ): Loan{
        return loanService.approveReturnRequest(loanId)
    }

    @PutMapping("/grab/{loanId}")
    @PreAuthorize("hasPermission(#loanId, '${ResourcePermission.RESOURCE_LOAN}', '${ResourcePermission.ACTION_GAB_BOOK}')")
    fun grabBook(
        @PathVariable loanId: String,
    ): ResponseEntity<Loan>{
        return ResponseEntity
            .status(200)
            .body(loanService.grabLoanBook(loanId))
    }

    @PutMapping("/devolution/{loanId}")
    @PreAuthorize("hasPermission(#loanId, '${ResourcePermission.RESOURCE_LOAN}', '${ResourcePermission.ACTION_BOOK_RETURN}')")
    fun bookReturn(
        @PathVariable loanId: String
    ): ResponseEntity<Loan>{
        return ResponseEntity
            .status(200)
            .body(loanService.bookReturn(loanId))
    }
}