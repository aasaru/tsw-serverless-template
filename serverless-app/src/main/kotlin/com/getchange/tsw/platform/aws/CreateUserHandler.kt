package com.getchange.tsw.platform.aws

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.getchange.tsw.Response
import com.getchange.tsw.controllers.UsersController
import com.getchange.tsw.servicegenerators.getUserService
import com.google.gson.Gson

class CreateUserHandler : RequestHandler<APIGatewayProxyRequestEvent, Response> {
    override fun handleRequest(event: APIGatewayProxyRequestEvent, context: Context?): Response {
        val payload = Gson().fromJson(event.body, Map::class.java)
        val userService = getUserService()
        val usersController = UsersController(userService)
        return usersController.createUser(payload["email"] as String)
    }
}
