package com.getchange.util

import com.getchange.authentication.app.PosgreSQLContainer
import com.getchange.tsw.getDatabaseConnection
import com.getchange.tsw.performMigrations
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestInstancePostProcessor
import java.io.File
import java.sql.Connection

class RespositoryTestRunner : TestInstancePostProcessor {

    init {
        val postgreSQLTestContainer: PosgreSQLContainer = startTestContainer()
        connect(postgreSQLTestContainer)
    }

    private fun connect(postgreSQLTestContainer: PosgreSQLContainer) {
        connection = getDatabaseConnection(
            "tswdatabase",
            "tswdatabase",
            postgreSQLTestContainer.containerIpAddress,
            postgreSQLTestContainer.firstMappedPort.toString(),
            postgreSQLTestContainer.databaseName
        )
    }

    private fun startTestContainer(): PosgreSQLContainer {
        val postgreSQLTestContainer: PosgreSQLContainer = PosgreSQLContainer()
            .withDatabaseName("tswdatabase")
            .withUsername("tswdatabase")
            .withPassword("tswdatabase")
        postgreSQLTestContainer.start()
        return postgreSQLTestContainer
    }

    override fun postProcessTestInstance(testInstance: Any?, context: ExtensionContext?) {
        doMigration()
    }

    companion object {
        lateinit var connection: Connection
    }

    private fun doMigration() {
        val loader = javaClass.classLoader
        val file = File(loader.getResource("migrationscripts").file)
        println(file.absolutePath)
        performMigrations(connection, file.absolutePath)
    }
}
