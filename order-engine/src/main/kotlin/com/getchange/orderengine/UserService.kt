package com.getchange.orderengine

class UserService(
    private val userRepository: UserRepository
) {
    fun createUser(email: String) {
        userRepository.createUser(email)
    }

    fun getUser(id: Long): UserEntity? {
        return userRepository.getUserById(id)
    }
}