package com.challenge.library.configuration

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserAuthenticationDetails (
    val id : String,
    val name: String,
    private val username : String,
    private val password: String,
    private val authorities: Set<GrantedAuthority>
): UserDetails {
    override fun getAuthorities()= authorities

    override fun getPassword(): String = password

    override fun getUsername(): String = username
}