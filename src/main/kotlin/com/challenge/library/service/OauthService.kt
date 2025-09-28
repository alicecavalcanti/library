package com.challenge.library.service

import com.challenge.library.configuration.UserAuthenticationDetails
import com.challenge.library.configuration.security.JwtProvider
import com.challenge.library.controller.dto.SignInRequestDTO
import com.challenge.library.controller.dto.TokenResponseDTO
import com.challenge.library.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class OauthService (val authenticationManager: AuthenticationManager,
                    val jwtProvider: JwtProvider,
                    val userRepository: UserRepository): UserDetailsService {

    fun login(
        login :SignInRequestDTO
    ): TokenResponseDTO{
        val authenticationToken = UsernamePasswordAuthenticationToken(login.username, login.password)
        val authentication = authenticationManager.authenticate(authenticationToken)
        val user = authentication.principal as UserAuthenticationDetails
        return TokenResponseDTO( accessToken = jwtProvider.generateToken(user.username, user.password, user.authorities.toSet())!!)
    }

    override fun loadUserByUsername(username: String?): UserAuthenticationDetails {
        val user = userRepository.findByUsername(username)
        return UserAuthenticationDetails(
            id = user.id!!,
            username = user.username,
            name = user.name,
            password = user.password,
            authorities = user.roles.map { GrantedAuthority({ it.name }) }.toSet()
        )
    }
}