package com.challenge.library.service

import com.challenge.library.controller.dto.*
import com.challenge.library.exception.*
import com.challenge.library.mapper.LoanRequestMapper
import com.challenge.library.model.Loan
import com.challenge.library.model.LoanStatus
import com.challenge.library.repository.LoanRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate


@Service
class LoanService (
    private val loanRepository: LoanRepository,
    private val bookService: BookService,
    private val userService: UserService,
    private val loanRequestMapper: LoanRequestMapper
){

    fun listAllLibraryLoans(pagination: Pageable): Page<Loan> {
        return loanRepository.findLoans(pagination)
    }

    fun listUserBookLoan(
        idUser: String,
        pagination: Pageable
    ): Page<Loan>? {
        val user = userService.findUserById(idUser)

        val loanAndReturn = user.id?.let { loanRepository.findAllByIdUser(it, pagination) }
        return loanAndReturn
    }

    fun findLoanById(idLoan: String): Loan{
        return loanRepository.findById(idLoan).orElseThrow{ LoanNotFoundException() }
    }

    fun registerLoad(loanBookDTO: LoanBookDTO) : Loan {
        val book = bookService.findBookById(loanBookDTO.idBook)

        val listLoansFound = loanRepository.findAllByIdBook(book.id)

        listLoansFound.map { it.checkIfStatusNotValid(LoanStatus.RETURNED) }
            //TODO: fazer um comportamento de validação de status para chamar em todos os outros metódos que usam esse comportamento

        val loanCreate= loanRequestMapper.map(loanBookDTO)

        val createLoan= loanRepository.save(loanCreate)

        return createLoan
    }

    fun approveLoanRequest(idLoan: String): Loan{
        var loanFound= findLoanById(idLoan)

        loanFound.checkIfStatusNotValid(LoanStatus.REQUESTED_LOAN)

        loanFound.status= LoanStatus.APPROVED
        this.loanRepository.save(loanFound)

        return loanFound

    }

    fun approveReturnRequest(idLoan: String): Loan {
        var loanFound= findLoanById(idLoan)

        loanFound.checkIfStatusNotValid(LoanStatus.REQUESTED_RETURN )

        loanFound.status= LoanStatus.RETURNED
        this.loanRepository.save(loanFound)

        return loanFound
    }

    fun grabBook(idLoan: String) : Loan{
        val loanFound= findLoanById(idLoan)
        loanFound.checkIfStatusNotValid(LoanStatus.APPROVED)

        loanFound.status = LoanStatus.CHECKED_OUT
        loanRepository.save(loanFound)

        return loanFound

    }

    fun bookReturn(
        idLoan: String
    ): Loan{
        val loanFound = findLoanById(idLoan)
        loanFound.checkIfStatusNotValid(LoanStatus.CHECKED_OUT)

        loanFound.status = LoanStatus.REQUESTED_RETURN
        loanFound.userReturnDate = LocalDate.now()
        // TODO: adicionar os comportamentos do model em suas classes em vez dos serviços.

        loanRepository.save(loanFound)

        return loanFound
    }
    fun findAllLoanMonth(): List<Loan>{
        val lastMonth= LocalDate.now().minusMonths(1)
        return loanRepository.findAllLoanMonth(lastMonth.monthValue)
    }

    fun fiveMostBorrowedBooks(): List<FiveMostBorrowedBooksDTO>{
        return loanRepository.fiveMostBorrowedBooks()
    }

    fun lateAndOnTimeReturns(): List<LateAndOnTimeReturnsDTO>{
        return loanRepository.findLateAndOnTimeReturns()
    }


}