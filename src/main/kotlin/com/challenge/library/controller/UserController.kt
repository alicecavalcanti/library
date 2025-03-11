package com.challenge.library.controller

import com.challenge.library.configuration.UserAuthenticationDetails
import com.challenge.library.configuration.security.JwtProvider
import com.challenge.library.controller.dto.*
import com.challenge.library.model.Notification
import com.challenge.library.model.User
import com.challenge.library.service.NotificationService
import com.challenge.library.service.UserService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
class UserController(
    private val userService: UserService,
    private val notificationService: NotificationService,
    private val authenticationManager: AuthenticationManager,
    val jwtProvider: JwtProvider
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

    @PostMapping("sign-in")
    fun signIn(
        @RequestBody loginRequest: SignInRequestDTO,
        response: HttpServletResponse
    ): TokenResponseDTO {
        val authenticationToken = UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
         val authentication = authenticationManager.authenticate(authenticationToken)
        val user = (authentication.principal as UserAuthenticationDetails)
        return TokenResponseDTO( accessToken = jwtProvider.generateToken(user.username, user.password, user.authorities.toSet())!!)
    }
}