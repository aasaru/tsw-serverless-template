package com.getchange.tsw

import java.util.Collections

data class Response(
    val statusCode: Int = 200,
    var body: String? = null,
    val headers: Map<String, String>? = Collections.emptyMap()
)
