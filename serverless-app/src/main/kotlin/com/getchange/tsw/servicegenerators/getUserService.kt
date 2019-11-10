package com.getchange.tsw.servicegenerators

import com.getchange.orderengine.UserService
import com.getchange.tsw.UsersRepository
import com.getchange.tsw.createConnectionFromEnvVars

fun getUserService(): UserService {
    val dbConnection = createConnectionFromEnvVars()
    val usersRepository = UsersRepository(dbConnection)
    return UserService(usersRepository)
}