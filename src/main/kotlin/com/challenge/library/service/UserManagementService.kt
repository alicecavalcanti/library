package com.challenge.library.service

import com.challenge.library.controller.dto.TokenResponseDTO
import com.challenge.library.controller.dto.UserRequestDTO
import com.challenge.library.mapper.UserRequestMapper
import com.challenge.library.model.Token
import com.challenge.library.model.User
import com.challenge.library.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserManagementService(
    private val userRequestMapper: UserRequestMapper,
    private val userRepository: UserRepository
){

    fun createAdminAccount(userAdmin: UserRequestDTO) : User {
        val userAdminMapper = userRequestMapper.map(userAdmin)
        return userRepository.save(userAdminMapper)
    }

    fun createLibraryAccount(userLibrary: UserRequestDTO): User{
        val userLibraryMapper = userRequestMapper.mapLibrary(userLibrary)
        return userRepository.save(userLibraryMapper)
    }

    fun createMemberAccount(userMember: UserRequestDTO): User{
        val userMemberMapper = userRequestMapper.mapMember(userMember)
        return userRepository.save(userMemberMapper)
    }

    fun loginUser(user: UserRequestDTO): TokenResponseDTO {
        val user = userRepository.findByUsernameAndPassword(user.username, user.password)
        return TokenResponseDTO(token = user.token)
    }

}