package com.challenge.library.controller

import com.challenge.library.controller.dto.LoanBookDTO
import com.challenge.library.exception.UserNotFoundException
import com.challenge.library.model.Book
import com.challenge.library.model.LoanAndReturn
import com.challenge.library.service.LoanAndReturnManagementService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
class LoanAndReturnManagementController(
    private val loanAndReturnManagementService: LoanAndReturnManagementService
) {

    @GetMapping("/list")
    fun listAllBooksLibrary(
        pagination: Pageable
    ): Page<Book>{
        return loanAndReturnManagementService.listAllBooksLibrary(pagination)
    }

    @GetMapping("/list/{idUser}")
    fun ListUserBookLoan(
        @PathVariable idUser: String,
        pagination: Pageable
    ): Page<LoanAndReturn>{
        return loanAndReturnManagementService.ListUserBookLoan(idUser, pagination)
    }

    @PostMapping("/register")
    fun registerLoad(
        @RequestBody loan: LoanBookDTO,
        uriComponent: UriComponentsBuilder
    ): ResponseEntity<LoanAndReturn>{

        val createLoan= loanAndReturnManagementService.registerLoad(loan)

        val uri = uriComponent.path("/loan").build().toUri()

        return ResponseEntity.created(uri).body(createLoan)
    }

    @PutMapping("/approve/{id}")
    @ResponseStatus(HttpStatus.CONFLICT)
    fun approveLoanRequest(
       @PathVariable id: String
    ): ResponseEntity<LoanAndReturn>{
        return ResponseEntity
                    .status(200)
                    .body(loanAndReturnManagementService.approveLoanRequest(id))
    }

    @PutMapping("/grab/{idEmprestimo}")
    fun grabBook(
        @PathVariable idEmprestimo: String
    ): ResponseEntity<LoanAndReturn>{
        return ResponseEntity
            .status(200)
            .body(loanAndReturnManagementService.grabBook(idEmprestimo))

    }

    @PutMapping("/devolution/{idEmprestimo}")
    @ExceptionHandler(UserNotFoundException::class)
    fun bookReturn(
        @PathVariable idEmprestimo: String
    ): ResponseEntity<LoanAndReturn>{
        return ResponseEntity
            .status(200)
            .body(loanAndReturnManagementService.bookReturn(idEmprestimo))
    }
}