package com.challenge.library.service

import com.challenge.library.exception.BookNotLateException
import com.challenge.library.model.Notification
import com.challenge.library.repository.NotificationRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val loanService: LoanService,
    private val bookService: BookService
) {
    fun  listAllUserNotifications(idUser: String, pagination: Pageable): Page<Notification>{
        return notificationRepository.findAllByUserId(idUser, pagination)
    }

    fun createNotifyDelay(idLoan: String): Notification{
        val loan = loanService.findLoanById(idLoan)
        val borrowedBook = bookService.findBookById(loan.idBook)
        val DELAY_MESSAGE= "A entrega do livro '${borrowedBook.titulo}' está atrasada!"

        if(loan.isReturnOnTime(LocalDate.now())) throw BookNotLateException()

        val newNotification = Notification(
            idUser = loan.idUser,
            mensagem = DELAY_MESSAGE
        )

        return notificationRepository.save(newNotification)
    }
}