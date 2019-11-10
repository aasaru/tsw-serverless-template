package com.getchange.orderengine

interface OrderRepository {
    fun createorder(order: Order)
    fun getOrderForUser(userId: Long)
}