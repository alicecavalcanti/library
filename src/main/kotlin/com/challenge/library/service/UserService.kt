package com.challenge.library.service

import com.challenge.library.controller.dto.*
import com.challenge.library.exception.ActionNotAllowedException
import com.challenge.library.exception.UserNotFoundException
import com.challenge.library.mapper.UserRequestMapper
import com.challenge.library.model.QuantityEachTypeUsers
import com.challenge.library.model.Roles
import com.challenge.library.model.User
import com.challenge.library.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDate


@Service
class UserService(
    private val userRequestMapper: UserRequestMapper,
    private val userRepository: UserRepository
): UserDetailsService{
    fun createPrivilegedUser(privilegedUser: SignUpRequestDTO) : User {
        val privilegedUserMapper = userRequestMapper.map(privilegedUser)
        privilegedUserMapper.password = BCryptPasswordEncoder().encode(privilegedUserMapper.password)
        return userRepository.save(privilegedUserMapper)
    }

    fun createMemberAccount(userMember: SignUpRequestDTO): User {
        val userMemberMapper = userRequestMapper.map(userMember)
        if (userMemberMapper.roles.any { it != Roles.MEMBER }){
            throw ActionNotAllowedException()
        }
        userMemberMapper.password = BCryptPasswordEncoder().encode(userMemberMapper.password)
        return userRepository.save(userMemberMapper)
    }

    fun findUserById(id: String): User{
        return userRepository.findById(id).orElseThrow{UserNotFoundException()}
    }


    fun totalNumberUsers(): QuantityEachTypeUsers {
       return QuantityEachTypeUsers(
           totalUser = userRepository.findAll().size,
           adminUser = userRepository.findByRoles(listOf(Roles.ADMIN)).size,
           libraryUser = userRepository.findByRoles(listOf(Roles.LIBRARY)).size,
           memberUser = userRepository.findByRoles(listOf(Roles.MEMBER)).size
       )
    }

    fun newUsersLastMonth():UserGrowthDTO{
        val lastMonth= LocalDate.now().minusMonths(1)
        val numberUsersMonth=userRepository.findAllLoanDate(lastMonth.monthValue).size
        return UserGrowthDTO(month = lastMonth.month, userGrowth = numberUsersMonth)
    }

}