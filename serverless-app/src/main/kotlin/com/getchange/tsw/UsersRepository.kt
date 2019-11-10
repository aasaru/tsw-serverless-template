package com.getchange.tsw

import com.getchange.orderengine.UserEntity
import com.getchange.orderengine.UserRepository
import java.sql.Connection

class UsersRepository(
    private val connection: Connection
) : UserRepository {

    override fun createUser(email: String) {
        val query = """
            INSERT INTO users (email)
            VALUES (?)
        """.trimIndent()
        val statement = connection.prepareStatement(query)
        statement.setString(1, email)
        statement.executeUpdate()
    }

    override fun getUserById(userId: Long): UserEntity? {
        val query = """
            SELECT * FROM users WHERE id=?
        """.trimIndent()
        val statement = connection.prepareStatement(query)
        statement.setLong(1, userId)
        val resultSet = statement.executeQuery()
        return if (resultSet.next()) {
            UserEntity(
                id = resultSet.getLong("id"),
                email = resultSet.getString("email")
            )
        } else null
    }
}