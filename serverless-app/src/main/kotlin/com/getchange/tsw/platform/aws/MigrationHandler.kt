package com.getchange.tsw.platform.aws

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.getchange.tsw.Response
import com.getchange.tsw.createConnectionFromEnvVars
import com.getchange.tsw.performMigrations

class MigrationHandler : RequestHandler<Any, Response> {
    override fun handleRequest(input: Any, context: Context?): Response {
        val postgresDataProvider = createConnectionFromEnvVars()
        performMigrations(postgresDataProvider, "migrationscripts")
        return Response(200)
    }
}
