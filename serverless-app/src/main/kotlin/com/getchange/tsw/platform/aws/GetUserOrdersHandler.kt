package com.getchange.tsw.platform.aws

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.getchange.tsw.Response
import com.getchange.tsw.controllers.OrdersController
import com.getchange.tsw.servicegenerators.getOrderService

class GetUserOrdersHandler : RequestHandler<APIGatewayProxyRequestEvent, Response> {
    override fun handleRequest(event: APIGatewayProxyRequestEvent, context: Context?): Response {
        val orderService = getOrderService()
        val ordersController = OrdersController(orderService)
        return event.pathParameters["id"]?.let {
            return ordersController.getUserOrders(it.toLong())
        } ?: Response(204)
    }
}
