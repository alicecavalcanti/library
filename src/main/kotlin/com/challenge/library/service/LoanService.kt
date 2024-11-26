package com.challenge.library.service

import com.challenge.library.controller.dto.*
import com.challenge.library.exception.*
import com.challenge.library.mapper.LoanRequestMapper
import com.challenge.library.model.Loan
import com.challenge.library.repository.LoanRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime


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
        return loanRepository.findAllByIdUser(user.id!!, pagination)
    }

    fun findLoanById(idLoan: String): Loan {
        return loanRepository.findById(idLoan).orElseThrow{ LoanNotFoundException() }
    }


    fun registerLoad(loanBookDTO: LoanBookDTO) : Loan {
        val book = bookService.findBookById(loanBookDTO.idBook)
        val listLoansFound = loanRepository.findAllByIdBook(book.id)
        if(listLoansFound.any{ it.isLoanNotReturned()}) throw BookAlreadyRequestedException()
        val loanCreate= loanRequestMapper.map(loanBookDTO)
        return loanRepository.save(loanCreate)
    }

    fun approveLoanRequest(idLoan: String): Loan{
        val loanFound = findLoanById(idLoan)
        if (!loanFound.approveLoan()) throw NotApprovedLoanException()
        return loanRepository.save(loanFound)
    }

    fun approveReturnRequest(idLoan: String): Loan {
        val loanFound= findLoanById(idLoan)
        if (!loanFound.approveReturn()) throw NotRequestedReturnLoanException()
        return loanRepository.save(loanFound)
    }

    fun grabLoanBook(idLoan: String) : Loan{
        val loanFound= findLoanById(idLoan)
        if (!loanFound.grabBook()) throw NotCheckedOutLoanException()
        loanRepository.save(loanFound)
        return loanFound
    }

    fun bookReturn(
        idLoan: String
    ): Loan{
        val loanFound = findLoanById(idLoan)
        if (!loanFound.requestbookReturn()) throw NotRequestedReturnLoanException()
        return loanRepository.save(loanFound)
    }

    fun findAllLoanMonth(): List<Loan>{
        val end = LocalDate.now().atTime(LocalTime.MAX)
        val start = LocalDate.now().atStartOfDay().minusMonths(1)
        return loanRepository.findLoansByPeriod(start, end)
    }

    fun fiveMostBorrowedBooks(): List<ThreeMostBorrowedBooksDTO>{
        return loanRepository.threeMostBorrowedBooks()
    }

    fun lateAndOnTimeReturns(): List<LateAndOnTimeReturnsDTO>{
        val loans = loanRepository.findAllLoansReturns()
        var loanOnTime = 0
        var lateLoan = 0

        loans.map { if(it.userReturnDate!! <= it.expectedReturnDate) loanOnTime += 1}
        loans.map { if(it.userReturnDate!! > it.expectedReturnDate) lateLoan += 1}

        return listOf (
            LateAndOnTimeReturnsDTO(
                loanOnTime = loanOnTime,
                lateLoan = lateLoan
            )
        )
    }

}