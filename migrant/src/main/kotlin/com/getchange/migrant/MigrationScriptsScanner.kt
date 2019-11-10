package com.getchange.migrant

import org.slf4j.LoggerFactory // ktlint-disable
import java.io.File // ktlint-disable
import java.io.FileNotFoundException // ktlint-disable
import java.lang.Exception // ktlint-disable

class MigrationScriptsScanner(
    private val directory: File
) {
    companion object {
        private val logger = LoggerFactory.getLogger(MigrationScriptsScanner::class.java)
    }

    fun getMigrationScripts(): List<MigrationScript> {
        if (!directory.exists())
            throw FileNotFoundException("Migration directory not found")
        return getMigrationScriptsFromFolder(directory)
    }

    private fun getMigrationScriptsFromFolder(directory: File): List<MigrationScript> {
        val folderFiles = directory.listFiles()
        val migrationScripts = ArrayList<MigrationScript>()
        folderFiles.forEach { file ->
            logger.info("Found file ${file.name}")
            handleFile(file, migrationScripts)
        }
        return migrationScripts
    }

    private fun handleFile(
        file: File,
        migrationScripts: ArrayList<MigrationScript>
    ) {
        if (file.isDirectory) migrationScripts.addAll(getMigrationScriptsFromFolder(file))
        else {
            if (file.name.endsWith(".sql"))
                migrationScripts.find { it.scriptName == file.name }?.let {
                    throw MigrationScriptNameClashException(
                        "File ${it.path} and ${file.path} names clash"
                    )
                } ?: migrationScripts.add(
                    MigrationScript(
                        migrationScript = getFileBytes(file),
                        scriptName = file.name,
                        path = file.path
                    )
                )
        }
    }

    private fun getFileBytes(file: File): String {
        return try {
            String(file.readBytes())
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}
