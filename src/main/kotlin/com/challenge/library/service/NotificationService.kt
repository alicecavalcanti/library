package com.challenge.library.service

import com.challenge.library.model.Notification
import com.challenge.library.repository.NotificationRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val bookService: BookService
) {
    fun  listAllUserNotifications(idUser: String, pagination: Pageable): Page<Notification>{
        return notificationRepository.findAllByUserId(idUser, pagination)
    }

    fun createNotifyDelay(userId: String, bookId: String): Notification {
        val borrowedBook = bookService.findBookById(bookId)
        val newNotification = Notification(
            userId = userId,
            message = "A entrega do livro '${borrowedBook.titulo}' está atrasada!"
        )
        return notificationRepository.save(newNotification)
    }
}