package com.challenge.library.configuration.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.challenge.library.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication

import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class JwtProvider(val userService: UserService) {

    @Value("\${security.token.secret}")
    private lateinit var secret: String

    fun generateToken(username: String, password: String, roles: Set<GrantedAuthority>): String? {
        try {
            val algorithm = Algorithm.HMAC256(secret)
            return JWT.create()
                .withIssuer("api-library")
                .withSubject(username)
                .withExpiresAt(getExpirationDate())
                .sign(algorithm)
        } catch (exception: JWTCreationException) {
            throw RuntimeException("Error while generating token", exception)
        }
    }

    fun validateToken(token: String): String {
        try {
            val algorithm = Algorithm.HMAC256(secret)
            return JWT.require(algorithm)
                .withIssuer("api-library")
                .build()
                .verify(token)
                .subject
        } catch (exception: JWTVerificationException) {
            throw RuntimeException("Error verifying token", exception)
        }
    }

    fun getExpirationDate(): Instant {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"))
    }

    fun getAuthentication(jwt: String): Authentication {
        val username = validateToken(jwt)
        val user = userService.loadUserByUsername(username)
        val userPrincipal = UserAuthenticationPrincipal(
            id = user.id,
            name = user.name,
            username = username
        )
        return UsernamePasswordAuthenticationToken(userPrincipal, null, user.authorities)
    }
}