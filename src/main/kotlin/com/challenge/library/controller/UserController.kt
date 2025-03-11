package com.challenge.library.controller

import com.challenge.library.controller.dto.*
import com.challenge.library.model.Notification
import com.challenge.library.model.User
import com.challenge.library.service.NotificationService
import com.challenge.library.service.UserService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
class UserController(
    private val userService: UserService,
    private val notificationService: NotificationService,
    private val loanService: LoanService,
    private val loanRepository: LoanRepository,
    private val bookRepository: BookRepository,
    private val bookService: BookService
){
    @PostMapping("/create-privileged-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun createPrivilegedUser(
        @RequestBody userRequestAdmin: SignUpRequestDTO,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<User>{
        val userCreated = userService.createPrivilegedUser(userRequestAdmin)
        val uriUser = uriBuilder.path("/create-privileged-user").build().toUri()
        return ResponseEntity.created(uriUser).body(userCreated)
    }

    @PostMapping("/create-member")
    fun createMemberAccount(
        @RequestBody @Valid userRequestMember: SignUpRequestDTO,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<User>{
        val userCreated = userService.createMemberAccount(userRequestMember)
        val uriUser = uriBuilder.path("/create-member").build().toUri()
        return ResponseEntity.created(uriUser).body(userCreated)
    }

    @GetMapping("/notification/{idUser}")
    fun listAllUserNotification(
        @PathVariable idUser: String,
        pagination: Pageable
    ): Page<Notification> {
        return notificationService.listAllUserNotifications(idUser, pagination)
    }
    @PostMapping("/notification-delay/{idLoan}")
    fun notifyDelay(
        @PathVariable idLoan: String
    ): Notification {
        return notificationService.createNotifyDelay(idLoan)
    }
}