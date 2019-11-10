package com.getchange.orderengine

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Users Service")
class UserServiceSpec {
    lateinit var userRepository: UserRepository
    lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        userRepository = mock()
        userService = UserService(userRepository)
    }

    @Test
    fun `creates a user with the repo`() {
        val email = "test@example.com"
        userService.createUser(email)
        verify(userRepository).createUser(email)
    }

    @Test
    fun `get get a user from the repo`() {
        val id = 1L
        val sample = UserEntity(
            id = id,
            email = "test@example.com"
        )
        whenever(userRepository.getUserById(id)).thenReturn(sample)
        val user = userService.getUser(id)
        verify(userRepository).getUserById(id)
        Assertions.assertNotNull(user)
        Assertions.assertEquals(sample.email, user!!.email)
    }
}