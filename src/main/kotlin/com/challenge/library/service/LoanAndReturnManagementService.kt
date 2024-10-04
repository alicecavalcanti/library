package com.challenge.library.service

import com.challenge.library.controller.dto.LoanBookDTO
import com.challenge.library.exception.*
import com.challenge.library.mapper.LoanRequestMapper
import com.challenge.library.model.Book
import com.challenge.library.model.LoanAndReturn
import com.challenge.library.model.LoanStatus
import com.challenge.library.repository.BookRepository
import com.challenge.library.repository.LoanAndReturnManagementRepository
import com.challenge.library.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class LoanAndReturnManagementService (
    private val loanAndReturnManagementRepository: LoanAndReturnManagementRepository,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val loanRequestMapper: LoanRequestMapper,
    private val userNotFoundException: UserNotFoundException,
    private val bookNotFoundException: BookNotFoundException,
    private val statusNotApprovedException: StatusNotApprovedException,
    private val statusNotCheckedOutException: StatusNotCheckedOutException,
    private val statusNotRequestedException: StatusNotRequestedException,
    private val bookCannotBeRequestedException: BookCannotBeRequestedException

){

    fun listAllBooksLibrary(pagination: Pageable): Page<Book> {
        return bookRepository.findAll(pagination)
    }
    fun ListUserBookLoan(
        idUser: String,
        pagination: Pageable
    ): Page<LoanAndReturn> {
        val loanAndReturn = loanAndReturnManagementRepository.findAllByIdUser(idUser, pagination)
        val user = userRepository.findById(idUser)

        if (user.isEmpty) throw userNotFoundException
        return loanAndReturn

    }
    fun registerLoad(loan: LoanBookDTO) : LoanAndReturn {
        val book = bookRepository.findById(loan.idBook)
        val user = userRepository.findById(loan.idUser)

        if (user.isEmpty) throw userNotFoundException
        if (book.isEmpty) throw bookNotFoundException

        val bookObject =book.orElseThrow()
        val bookFoundLoan = loanAndReturnManagementRepository.findAllByIdBook(bookObject.id)

        bookFoundLoan.forEach() {
            if (it.status != LoanStatus.RETURNED) throw bookCannotBeRequestedException
        }

        val loanCreate= loanRequestMapper.map(loan)
        val createLoan= loanAndReturnManagementRepository.save(loanCreate)

        return createLoan
    }

    fun approveLoanRequest(id: String): LoanAndReturn {
        var emprestimo= loanAndReturnManagementRepository.findById(id).orElseThrow()

        if(emprestimo.status != LoanStatus.REQUESTED) throw statusNotRequestedException

        emprestimo.status= LoanStatus.APPROVED
        loanAndReturnManagementRepository.save(emprestimo)

        return emprestimo

    }

    fun grabBook(idEmprestimo: String) : LoanAndReturn{
        val book= loanAndReturnManagementRepository.findById(idEmprestimo).orElseThrow()
        if(book.status != LoanStatus.APPROVED) throw statusNotApprovedException

        book.status = LoanStatus.CHECKEDOUT
        loanAndReturnManagementRepository.save(book)

        return book

    }

    fun bookReturn(idEmprestimo: String): LoanAndReturn{
        val book = loanAndReturnManagementRepository.findById(idEmprestimo).orElseThrow()
        if (book.status != LoanStatus.CHECKEDOUT) throw statusNotCheckedOutException

        book.status = LoanStatus.RETURNED
        loanAndReturnManagementRepository.save(book)
        return book
    }




}