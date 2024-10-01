package com.challenge.library.controller

import com.challenge.library.controller.dto.TokenResponseDTO
import com.challenge.library.controller.dto.UserRequestDTO
import com.challenge.library.model.User
import com.challenge.library.service.UserManagementService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
class UserManagementController(
    private val userManagementService: UserManagementService
){

    @PostMapping("/createAdmin")
    fun createAdminAccount(
        @RequestBody @Valid userRequestAdminDTO: UserRequestDTO,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<User>{
        val userCreated = userManagementService.createAdminAccount(userRequestAdminDTO)
        val uriUser = uriBuilder.path("/createAdmin").build().toUri()
        return ResponseEntity.created(uriUser).body(userCreated)

    }

    @PostMapping("/createLibrary")
    fun createLibraryAccount(
        @RequestBody @Valid userRequestLibraryDTO: UserRequestDTO,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<User>{
        val userCreated = userManagementService.createLibraryAccount(userRequestLibraryDTO)
        val uriUser = uriBuilder.path("/createAdmin").build().toUri()
        return ResponseEntity.created(uriUser).body(userCreated)
    }

    @PostMapping("/createMember")
    fun createMemberAccount(
        @RequestBody userMember: UserRequestDTO,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<User>{
        val userCreated = userManagementService.createMemberAccount(userMember)
        val uriUser = uriBuilder.path("/createAdmin").build().toUri()
        return ResponseEntity.created(uriUser).body(userCreated)
    }

    @PostMapping("/signIn")
    fun login(
        @RequestBody user: UserRequestDTO
    ): TokenResponseDTO{
        return userManagementService.loginUser(user)
    }

}