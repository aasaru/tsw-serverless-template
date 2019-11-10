package com.getchange.tsw

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler

class MigrationHandler : RequestHandler<Any, Response> {
    override fun handleRequest(input: Any, context: Context?): Response {
        val postgresDataProvider = getDataProvider()
        performMigrations(postgresDataProvider, "migrationscripts")
        return Response(200)
    }
}
