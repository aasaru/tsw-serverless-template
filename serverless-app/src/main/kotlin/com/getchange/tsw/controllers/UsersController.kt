package com.getchange.tsw.controllers

import com.getchange.orderengine.UserService
import com.getchange.tsw.Response
import com.google.gson.Gson

class UsersController(
    private val userService: UserService
) {
    fun createUser(
        email: String
    ): Response {
        return try {
            userService.createUser(email)
            Response(200)
        } catch (e: Exception) {
            e.printStackTrace()
            Response(500)
        }
    }

    fun getUser(
        id: Long
    ): Response {
        return try {
            val user = userService.getUser(id)
            Response(200, body = Gson().toJson(user))
        } catch (e: Exception) {
            e.printStackTrace()
            Response(500)
        }
    }
}