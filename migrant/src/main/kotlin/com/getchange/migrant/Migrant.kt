package com.getchange.migrant

import org.slf4j.LoggerFactory // ktlint-disable
import java.sql.Connection // ktlint-disable
import java.sql.ResultSet // ktlint-disable
import java.util.Calendar // ktlint-disable

class Migrant(
    migrationScriptsScanner: MigrationScriptsScanner,
    private val connection: Connection
) {
    companion object {
        private val logger = LoggerFactory.getLogger(MigrationScriptsScanner::class.java)
    }

    var migrationFiles: List<MigrationScript>

    init {
        createHistoryTableIfNotExists(connection)
        migrationFiles = migrationScriptsScanner.getMigrationScripts()
    }

    fun beginMigration() {
        val migrationsToRun = validateScriptsUsingHistory(connection, migrationFiles)
        connection.autoCommit = false
        migrationsToRun.sortedBy { it.scriptName }
        migrationsToRun.sortedBy { it.scriptName }.forEach {
            println("Running ${it.migrationScript}")
            connection.createStatement().execute(it.migrationScript)
            saveToHistory(it)
        }
        connection.commit()
    }

    private fun saveToHistory(it: MigrationScript) {
        val query = """
                    INSERT INTO ${MigrantOptions.migrantHistoryTableName} 
                    (
                    ${MigrantOptions.migrantVersionFieldName}, 
                    ${MigrantOptions.scriptNameFieldName}, 
                    ${MigrantOptions.checkSumFieldName},
                    ${MigrantOptions.runByFieldName},
                    ${MigrantOptions.timeRunFieldName})
                    VALUES (?, ?, ?, ?, ?)
                """.trimIndent()

        val statement = connection.prepareStatement(query)
        statement.setString(1, "1.0")
        statement.setString(2, it.scriptName)
        statement.setString(3, it.getHash())
        statement.setString(4, connection.metaData.userName)
        statement.setLong(5, Calendar.getInstance().timeInMillis)
        statement.execute()
    }

    private fun validateScriptsUsingHistory(connection: Connection, scripts: List<MigrationScript>): List<MigrationScript> {
        return scripts.filter { script ->
            val result = getScriptFromHistory(connection, script)
            result?.let {
                if (it.next())
                    if (script.getHash() != it.getString("checksum")) throw InvalidHashException()
                    else false
                else true
            } ?: true
        }
    }

    private fun getScriptFromHistory(
        connection: Connection,
        script: MigrationScript
    ): ResultSet? {
        val statement = connection.createStatement()
        return statement.executeQuery(
            """
                SELECT * FROM ${MigrantOptions.migrantHistoryTableName} WHERE script_name = '${script.scriptName}' 
                LIMIT 1
                """.trimIndent()
        )
    }

    private fun createHistoryTableIfNotExists(
        connection: Connection
    ) {
        if (!isHistoryTableExistent(connection))
            createHistoryTable()
        else {
            logger.info("History table exists...")
        }
    }

    private fun createHistoryTable() {
        val query = """
            CREATE TABLE IF NOT EXISTS ${MigrantOptions.migrantHistoryTableName}
            (
                ${MigrantOptions.scriptNameFieldName} VARCHAR(255) NOT NULL,
                ${MigrantOptions.checkSumFieldName} VARCHAR(255) NOT NULL,
                ${MigrantOptions.migrantVersionFieldName} VARCHAR(20) NOT NULL,
                ${MigrantOptions.runByFieldName} VARCHAR(255) NOT NULL,
                ${MigrantOptions.timeRunFieldName} BIGINT NOT NULL
            );
        """.trimIndent()
        connection.createStatement().execute(query)
    }

    private fun isHistoryTableExistent(connection: Connection): Boolean {
        val metadata = connection.metaData
        val result = metadata.getTables(null, null, MigrantOptions.migrantHistoryTableName, null)
        return (result.fetchSize > 0)
    }
}
