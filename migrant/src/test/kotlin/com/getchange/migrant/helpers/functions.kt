package com.getchange.migrant.helpers

import java.math.BigInteger
import java.security.MessageDigest

fun convertToSha1(text: String): String {
    val hasher = MessageDigest.getInstance("SHA-1")
    hasher.reset()
    hasher.update(text.toByteArray())
    return BigInteger(1, hasher.digest()).toString(16)
}
