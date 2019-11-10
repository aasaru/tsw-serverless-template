package com.getchange.orderengine

interface UserRepository {
    fun getUserById(userId: Long): User
}