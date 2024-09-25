package com.challenge.library.configuration

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.GenericContainer


@TestConfiguration(proxyBeanMethods = false)
class ConfigurationContainerizedRedisTest : BeforeAllCallback {

    @Bean
    @ServiceConnection(name = "redis")
    fun redisContainer(): GenericContainer<*> {
        return GenericContainer("redis:latest")
            .withExposedPorts(6379);
    }
    override fun beforeAll(p0: ExtensionContext?) {
        redisContainer().start()
    }
}

