package com.challenge.library.configuration


import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@DataMongoTest
@Testcontainers
abstract class AbstractContainerizedMongoTest {
    companion object {
        @JvmStatic
        @Container
        @ServiceConnection
        var mongoDBContainer = MongoDBContainer("mongo:latest")
            .withExposedPorts(27017)
    }
}