package com.getchange.controllers

import com.getchange.orderengine.UserEntity
import com.getchange.orderengine.UserService
import com.getchange.tsw.controllers.UsersController
import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Users Controller")
class UserContollerSpec {

    lateinit var userService: UserService
    lateinit var usersController: UsersController

    @BeforeEach
    fun setUp() {
        userService = mock()
        usersController = UsersController(userService)
    }

    @Test
    fun `POST users returns 200 OK if all goes well`() {
        val userEmail = "test@example.com"
        val response = usersController.createUser(userEmail)
        verify(userService).createUser(userEmail)
        Assertions.assertEquals(200, response.statusCode)
    }

    @Test
    fun `POST users returns 500 internal server error is error is thrown`() {
        val userEmail = "test@example.com"
        whenever(userService.createUser(userEmail)).then {
            throw Exception()
        }
        val response = usersController.createUser(userEmail)
        verify(userService).createUser(userEmail)
        Assertions.assertEquals(500, response.statusCode)
    }

    @Test
    fun `GET users by ID returns 200 OK when all is well`() {
        val userEmail = "test@example.com"
        val sampleUser = UserEntity(
            email = userEmail
        )
        val userId = 1L
        whenever(userService.getUser(userId)).thenReturn(sampleUser)
        val response = usersController.getUser(userId)
        verify(userService).getUser(userId)
        Assertions.assertEquals(200, response.statusCode)
        Assertions.assertEquals(sampleUser, Gson().fromJson(response.body, UserEntity::class.java))
    }
}