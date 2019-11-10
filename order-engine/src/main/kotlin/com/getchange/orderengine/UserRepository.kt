package com.getchange.orderengine

interface UserRepository {
    fun createUser(email: String)
    fun getUserById(userId: Long): UserEntity?
}