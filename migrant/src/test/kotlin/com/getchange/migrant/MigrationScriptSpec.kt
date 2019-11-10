package com.getchange.migrant

import org.junit.jupiter.api.Assertions // ktlint-disable
import org.junit.jupiter.api.DisplayName // ktlint-disable
import org.junit.jupiter.api.Test // ktlint-disable
import com.getchange.migrant.helpers.convertToSha1 // ktlint-disable

@DisplayName("Migration Script")
class MigrationScriptSpec {

    @Test
    fun `Returns correct hash`() {
        val migrationScript = MigrationScript(
            migrationScript = "migration",
            scriptName = "script_name",
            path = "script_name"
        )

        val hash = migrationScript.getHash()
        val expectedHash = convertToSha1("${migrationScript.migrationScript}")
        Assertions.assertEquals(expectedHash, hash)
    }
}
