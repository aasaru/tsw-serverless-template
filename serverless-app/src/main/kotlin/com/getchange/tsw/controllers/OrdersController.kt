package com.getchange.tsw.controllers

import com.getchange.orderengine.Currency
import com.getchange.orderengine.OrderService
import com.getchange.tsw.Response
import com.google.gson.Gson

class OrdersController(
    private val orderService: OrderService
) {
    fun buy(userId: Long, amount: Double, currency: String): Response {
        orderService.createBuyOrder(
            userId = userId,
            amount = amount,
            currency = Currency.valueOf(currency)
        )
        return Response(201)
    }

    fun sell(userId: Long, currency: String, amount: Double): Response {
        orderService.createSellOrder(
            userId = userId,
            currency = Currency.valueOf(currency),
            amount = amount
        )
        return Response(201)
    }

    fun convert(userId: Long, from: String, to: String, amount: Double): Response {
        orderService.convert(
            userId, amount, Currency.valueOf(from), Currency.valueOf(to)
        )
        return Response(201)
    }

    fun getUserOrders(userId: Long): Response {
        val orders = orderService.listOrdersForUser(userId)
        return Response(
            200,
            Gson().toJson(orders)
        )
    }
}