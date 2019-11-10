package com.getchange.migrant

import java.math.BigInteger
import java.security.MessageDigest

data class MigrationScript(
    val migrationScript: String,
    val scriptName: String,
    val path: String
) {
    fun getHash(): String {
        return convertToSha1("$migrationScript")
    }

    private fun convertToSha1(text: String): String {
        val hasher = MessageDigest.getInstance("SHA-1")
        hasher.reset()
        hasher.update(text.toByteArray())
        return BigInteger(1, hasher.digest()).toString(16)
    }
}
