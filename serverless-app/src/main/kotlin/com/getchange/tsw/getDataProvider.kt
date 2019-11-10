package com.getchange.tsw

import java.sql.Connection
import java.sql.DriverManager
import java.util.Properties

fun getDatabaseConnection(
    user: String,
    password: String,
    host: String,
    port: String,
    database: String
): Connection {
    val props = Properties()
    props.setProperty("user", user)
    props.setProperty("password", password)

    var url = "jdbc:postgresql://$host:$port/$database"
    println("Connecting to $url")
    return DriverManager.getConnection(url, props)
}

fun createConnectionFromEnvVars(): Connection {
    val host = System.getenv("DB_HOST")
    val user = System.getenv("DB_USER")
    val port = System.getenv("DB_PORT")
    val database = System.getenv("DB_DATABASE")
    val password = System.getenv("DB_PASSWORD")
    return getDatabaseConnection(
        user,
        password,
        host,
        port,
        database
    )
}
