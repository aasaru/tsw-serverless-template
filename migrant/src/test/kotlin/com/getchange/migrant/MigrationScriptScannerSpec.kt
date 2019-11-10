package com.getchange.migrant

import com.nhaarman.mockito_kotlin.doReturn // ktlint-disable
import com.nhaarman.mockito_kotlin.mock // ktlint-disable
import com.nhaarman.mockito_kotlin.verify // ktlint-disable
import com.nhaarman.mockito_kotlin.whenever // ktlint-disable
import org.junit.jupiter.api.Assertions // ktlint-disable
import org.junit.jupiter.api.BeforeEach // ktlint-disable
import org.junit.jupiter.api.DisplayName // ktlint-disable
import org.junit.jupiter.api.Test // ktlint-disable
import java.io.File // ktlint-disable
import java.io.FileNotFoundException // ktlint-disable

@DisplayName("Migration Script Scanner")
class MigrationScriptScannerSpec {
    private lateinit var migrationScriptsScanner: MigrationScriptsScanner
    private lateinit var directory: File
    private lateinit var file1: File
    private lateinit var file2: File
    private lateinit var file3: File
    private lateinit var file4: File

    @BeforeEach
    fun setUp() {
        directory = mock()
        whenever(directory.exists()).thenReturn(true)
        file1 = mock()
        file2 = mock()
        file3 = mock()
        file4 = mock()
        whenever(file1.name).thenReturn("file1.sql")
        whenever(file1.path).thenReturn("file1.sql")
        whenever(file2.name).thenReturn("file2.sql")
        whenever(file2.path).thenReturn("file2.sql")
        whenever(file3.name).thenReturn("file3")
        whenever(file3.path).thenReturn("file3")
        whenever(file4.name).thenReturn("file4.sql")
        whenever(file4.path).thenReturn("file4.sql")
        whenever(directory.listFiles()).thenReturn(listOf(
            file1, file2, file3, file4
        ).toTypedArray())
        migrationScriptsScanner = MigrationScriptsScanner(directory)
    }

    @Test
    fun `Checks if the migration folder exists and throws exception if not`() {
        whenever(directory.exists()).thenReturn(false)
        Assertions.assertThrows(FileNotFoundException::class.java) {
            migrationScriptsScanner.getMigrationScripts()
        }
        verify(directory).exists()
    }

    @Test
    fun `Loads only the sql files`() {
        val migrationScripts = migrationScriptsScanner.getMigrationScripts()
        Assertions.assertEquals(3, migrationScripts.size)
    }

    @Test
    fun `Can navigate the folder structure`() {
        setUpMultiFolderStructure()
        val migrationScripts = migrationScriptsScanner.getMigrationScripts()
        Assertions.assertEquals(3, migrationScripts.size)
    }

    @Test
    fun `Throws exception when there's a name clash`() {
        whenever(file2.name).thenReturn("file1.sql")
        setUpMultiFolderStructure()
        Assertions.assertThrows(MigrationScriptNameClashException::class.java) {
            migrationScriptsScanner.getMigrationScripts()
        }
    }

    private fun setUpMultiFolderStructure() {
        val folder1: File = mock {
            on { isDirectory } doReturn true
            on { name } doReturn "folder1"
            on { listFiles() } doReturn arrayOf(file1, file2)
        }

        val folder2: File = mock {
            on { isDirectory } doReturn true
            on { name } doReturn "folder2"
            on { listFiles() } doReturn arrayOf(file3, file4)
        }

        whenever(directory.listFiles()).thenReturn(
            listOf(
                folder1, folder2
            ).toTypedArray()
        )
        migrationScriptsScanner = MigrationScriptsScanner(directory)
    }
}
