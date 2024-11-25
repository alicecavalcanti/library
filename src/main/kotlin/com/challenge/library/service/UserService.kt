package com.challenge.library.service

import com.challenge.library.controller.dto.*
import com.challenge.library.exception.UserNotFoundException
import com.challenge.library.mapper.UserRequestMapper
import com.challenge.library.model.QuantityEachTypeUsers
import com.challenge.library.model.Roles
import com.challenge.library.model.User
import com.challenge.library.repository.UserRepository

import org.springframework.stereotype.Service
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDate


@Service
class UserService(
    private val userRequestMapper: UserRequestMapper,
    private val userRepository: UserRepository,
){
    private val logger: Logger = LoggerFactory.getLogger(UserService::class.java)

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