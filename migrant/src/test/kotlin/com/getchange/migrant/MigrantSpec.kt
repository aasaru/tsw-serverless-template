package com.getchange.migrant

import com.getchange.migrant.testcontainers.PosgreSQLContainer // ktlint-disable
import com.nhaarman.mockito_kotlin.doReturn // ktlint-disable
import com.nhaarman.mockito_kotlin.mock // ktlint-disable
import org.junit.jupiter.api.Assertions // ktlint-disable
import org.junit.jupiter.api.BeforeEach // ktlint-disable
import org.junit.jupiter.api.DisplayName // ktlint-disable
import org.junit.jupiter.api.Test // ktlint-disable
import java.sql.Connection // ktlint-disable
import com.nhaarman.mockito_kotlin.verify // ktlint-disable
import org.testcontainers.junit.jupiter.Container // ktlint-disable
import org.testcontainers.junit.jupiter.Testcontainers // ktlint-disable
import java.sql.DriverManager // ktlint-disable
import java.util.Calendar // ktlint-disable
import java.util.Properties // ktlint-disable

@DisplayName("Migrant")
@Testcontainers
class MigrantSpec {
    private lateinit var migrant: Migrant
    private lateinit var migrationScriptsScanner: MigrationScriptsScanner
    private lateinit var connection: Connection

    @Container
    val postgreSQLTestContainer: PosgreSQLContainer = PosgreSQLContainer()
        .withDatabaseName("migrant")
        .withUsername("migrant")
        .withPassword("migrant")

    @BeforeEach
    fun setUp() {
        migrationScriptsScanner = mock {
            on { getMigrationScripts() } doReturn getMigrationScripts()
        }
        val props = Properties()
        props.setProperty("user", "migrant")
        props.setProperty("password", "migrant")
        connection = DriverManager.getConnection(postgreSQLTestContainer.jdbcUrl, props)
        migrant = Migrant(migrationScriptsScanner, connection)
    }

    private fun getMigrationScripts(): List<MigrationScript> {
        val scriptOne = MigrationScript(
            migrationScript = "migrationScriptBody1",
            scriptName = "migrationScript1",
            path = "migrationScript1"
        )

        return listOf(
            scriptOne,
            scriptOne.copy(migrationScript = "migrationScriptBody2", scriptName = "migrationScript2"),
            scriptOne.copy(migrationScript = "migrationScriptBody3", scriptName = "migrationScript3")
        )
    }

    @Test
    fun `Creates history table`() {
        assertTableExists(MigrantOptions.migrantHistoryTableName)
    }

    @Test
    fun `Loads up the migration scripts`() {
        verify(migrationScriptsScanner).getMigrationScripts()
    }

    @Test
    fun `Throws exception when migration script hashes don't match history`() {
        insertMigrationHistory()
        val messedUpMigrationsScriptList = getMigrationScripts().map {
            it.copy(migrationScript = "nothing")
        }

        migrationScriptsScanner = mock {
            on { getMigrationScripts() } doReturn messedUpMigrationsScriptList
        }
        migrant = Migrant(migrationScriptsScanner, connection)
        Assertions.assertThrows(InvalidHashException::class.java) {
            migrant.beginMigration()
        }
    }

    @Test
    fun `Runs the scripts not found in the history`() {
        runFooBarTableMigration()
        assertTableExists("foobar")
    }

    @Test
    fun `Logs saved migration in the history database`() {
        val hash = runFooBarTableMigration()
        val query = """
            SELECT * FROM ${MigrantOptions.migrantHistoryTableName}
            WHERE ${MigrantOptions.scriptNameFieldName} = 'add_foobar_table.sql'
        """.trimIndent()
        val result = connection.createStatement().executeQuery(query)
        Assertions.assertTrue(result.next())
        Assertions.assertEquals(hash, result.getString(MigrantOptions.checkSumFieldName))
    }

    private fun insertMigrationHistory() {
        getMigrationScripts().forEach {
            val query = """
                 INSERT INTO ${MigrantOptions.migrantHistoryTableName} 
                (
                ${MigrantOptions.migrantVersionFieldName}, 
                ${MigrantOptions.scriptNameFieldName}, 
                ${MigrantOptions.checkSumFieldName},
                ${MigrantOptions.runByFieldName},
                ${MigrantOptions.timeRunFieldName})
                VALUES ('1.0', '${it.scriptName}', '${it.getHash()}', '${connection.metaData.userName}', '${Calendar.getInstance().timeInMillis}')
            """.trimIndent()
            connection.createStatement().execute(query)
        }
    }

    private fun assertTableExists(tableName: String) {
        val query = """
                SELECT EXISTS(
                    SELECT * 
                    FROM information_schema.tables 
                    WHERE
                      table_name = '$tableName'
                );
            """.trimIndent()
        val results = connection.createStatement().executeQuery(query)
        Assertions.assertTrue(results.next())
        Assertions.assertTrue(results.getBoolean("EXISTS"))
    }

    private fun runFooBarTableMigration(): String {
        insertMigrationHistory()
        val query = """
                CREATE TABLE foobar
                (
                    foo VARCHAR(255) NOT NULL,
                    bar VARCHAR(255) NOT NULL
                );
            """.trimIndent()
        val migrationScripts = ArrayList(getMigrationScripts())
        val tableMigration = MigrationScript(
            scriptName = "add_foobar_table.sql",
            migrationScript = query,
            path = "add_foobar_table.sql"
        )
        migrationScripts.add(tableMigration)
        migrationScriptsScanner = mock {
            on { getMigrationScripts() } doReturn migrationScripts
        }
        migrant = Migrant(migrationScriptsScanner, connection)
        migrant.beginMigration()
        return tableMigration.getHash()
    }
}
