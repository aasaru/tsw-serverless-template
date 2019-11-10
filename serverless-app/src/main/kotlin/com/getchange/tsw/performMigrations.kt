package com.getchange.tsw

import com.getchange.migrant.Migrant
import com.getchange.migrant.MigrationScriptsScanner
import java.io.File
import java.sql.Connection

fun performMigrations(connection: Connection, directoryPath: String) {
    val directory = File(directoryPath)
    val migrationScriptsScanner = MigrationScriptsScanner(directory)
    val migrant = Migrant(migrationScriptsScanner, connection)
    migrant.beginMigration()
}
