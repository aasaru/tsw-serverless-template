package com.getchange.tsw.platform.aws

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.getchange.tsw.Response
import com.getchange.tsw.controllers.OrdersController
import com.getchange.tsw.platform.aws.dto.CreateConvertOrderDto
import com.getchange.tsw.servicegenerators.getOrderService
import com.google.gson.Gson

class CreateConvertOrderHandler : RequestHandler<APIGatewayProxyRequestEvent, Response> {
    override fun handleRequest(event: APIGatewayProxyRequestEvent, context: Context?): Response {
        val payload = Gson().fromJson(event.body, CreateConvertOrderDto::class.java)
        val orderService = getOrderService()
        val ordersController = OrdersController(orderService)
        return ordersController.convert(
            userId = payload.userId,
            amount = payload.amount,
            from = payload.fromCurrency,
            to = payload.toCurrency
        )
    }
}
