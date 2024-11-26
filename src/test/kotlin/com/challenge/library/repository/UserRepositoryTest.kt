package com.challenge.library.repository

import com.challenge.library.model.Roles
import com.challenge.library.model.User
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDate

@DataMongoTest
@Testcontainers
class UserRepositoryTest{
    @Autowired
    lateinit var userRepository : UserRepository

    companion object{
        @JvmStatic
        @Container
        @ServiceConnection
        var mongoDBContainer = MongoDBContainer("mongo:latest")
            .withExposedPorts(27017)
    }

    @BeforeEach
    fun setup(){
        userRepository.deleteAll()
    }

    @Test
    fun `Quando o usuário dizer o mês, então deve retornar os meses que forem iguais`(){
        val user1= User(
            id = "1",
            username = "alicecavalcanti",
            password = "123",
            registrationDate = LocalDate.of(2022, 5, 12),
            roles = listOf(Roles.ADMIN)
        )
        val user2= User(
            id = "2",
            username = "danicavalcanti",
            password = "124",
            registrationDate = LocalDate.of(2024, 6, 23),
            roles = listOf(Roles.ADMIN)
        )
        val user3= User(
            id = "3",
            username = "lucasSilva",
            password = "124",
            registrationDate = LocalDate.of(2025, 6, 15),
            roles = listOf(Roles.ADMIN)
        )

        val users = listOf(user1, user2, user3)
        userRepository.saveAll(users)
        val result = userRepository.findAllLoanDate(6)

        Assertions.assertThat(result.size).isEqualTo(2)
        Assertions.assertThat(result[0].id).isEqualTo(user2.id)
        Assertions.assertThat(result[0].username).isEqualTo(user2.username)
        Assertions.assertThat(result[1].id).isEqualTo(user3.id)
        Assertions.assertThat(result[1].username).isEqualTo(user3.username)
    }

    @Test
    fun `Quando o usuário dizer o mês, então não deve encontrar usuários nesse mês`(){
        val user1= User(
            id = "1",
            username = "alicecavalcanti",
            password = "123",
            registrationDate = LocalDate.of(2022, 5, 12),
            roles = listOf(Roles.ADMIN)
        )
        val user2= User(
            id = "2",
            username = "danicavalcanti",
            password = "124",
            registrationDate = LocalDate.of(2024, 6, 23),
            roles = listOf(Roles.ADMIN)
        )

        val users = listOf(user1, user2)
        userRepository.saveAll(users)
        val result = userRepository.findAllLoanDate(7)

        Assertions.assertThat(result).isEmpty()
    }
}