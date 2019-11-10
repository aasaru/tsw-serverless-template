package com.getchange.tsw.platform.aws

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.getchange.tsw.Response
import com.getchange.tsw.controllers.OrdersController
import com.getchange.tsw.platform.aws.dto.CreateBuySellOrderDto
import com.getchange.tsw.servicegenerators.getOrderService
import com.google.gson.Gson

class CreateSellOrderHandler : RequestHandler<APIGatewayProxyRequestEvent, Response> {
    override fun handleRequest(event: APIGatewayProxyRequestEvent, context: Context?): Response {
        val payload = Gson().fromJson(event.body, CreateBuySellOrderDto::class.java)
        val orderService = getOrderService()
        val ordersController = OrdersController(orderService)
        return ordersController.sell(
            userId = payload.userId,
            amount = payload.amount,
            currency = payload.currency
        )
    }
}
