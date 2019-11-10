package com.getchange.tsw.servicegenerators

import com.getchange.orderengine.OrderService
import com.getchange.tsw.OrdersRepository
import com.getchange.tsw.createConnectionFromEnvVars

fun getOrderService(): OrderService {
    val dbConnection = createConnectionFromEnvVars()
    val ordersRepository = OrdersRepository(dbConnection)
    return OrderService(ordersRepository)
}