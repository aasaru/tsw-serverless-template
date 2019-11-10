package com.getchange.orderengine

interface OrderRepository {
    fun createorder(order: OrderEntity)
    fun getOrdersForUser(userId: Long): List<OrderEntity>
}