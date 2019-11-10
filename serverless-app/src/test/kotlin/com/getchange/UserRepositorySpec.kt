package com.getchange

import com.getchange.tsw.UsersRepository
import com.getchange.util.RespositoryTestRunner
import com.getchange.util.insertTestUser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.sql.Connection

@DisplayName("User Repository")
@ExtendWith(RespositoryTestRunner::class)
class UserRepositorySpec {

    lateinit var connection: Connection
    lateinit var usersRepository: UsersRepository

    @BeforeEach
    fun setUp() {
        connection = RespositoryTestRunner.connection
        usersRepository = UsersRepository(connection)
    }

    @Test
    fun `can get user`() {
        val id = insertTestUser(connection)
        val user = usersRepository.getUserById(id)
        Assertions.assertNotNull(user)
        Assertions.assertEquals("test@example.com", user!!.email)
    }

    @Test
    fun `can create user`() {
        val email = "test@example.com"
        usersRepository.createUser(email)
        val q = """
            SELECT * FROM users
        """.trimIndent()
        val rs = connection.createStatement().executeQuery(q)
        Assertions.assertTrue(rs.next())
        Assertions.assertEquals(email, rs.getString("email"))
    }
}