package com.getchange.util

import java.sql.Connection
import java.sql.Statement

fun insertTestUser(connection: Connection): Long {
    val query = """
            INSERT INTO users (email)
            VALUES ('test@example.com')
        """.trimIndent()
    val statement = connection.createStatement()
    statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS)
    return if (statement.generatedKeys.next())
        statement.generatedKeys.getLong(1)
    else -1
}