package com.getchange.tsw.platform.aws

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.getchange.tsw.Response
import com.getchange.tsw.controllers.UsersController
import com.getchange.tsw.servicegenerators.getUserService

class GetUserHandler : RequestHandler<APIGatewayProxyRequestEvent, Response> {
    override fun handleRequest(event: APIGatewayProxyRequestEvent, context: Context?): Response {
        val userService = getUserService()
        val usersController = UsersController(userService)
        return event.pathParameters["id"]?.let {
            return usersController.getUser(it.toLong())
        } ?: Response(204)
    }
}
