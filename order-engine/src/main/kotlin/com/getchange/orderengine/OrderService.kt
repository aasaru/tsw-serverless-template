package com.getchange.orderengine

import java.math.BigDecimal
import java.util.Date

class OrderService(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository
) {
    fun createBuyOrder(
        userId: Long,
        amount: Double,
        currency: Currency
    ) {
        val user = userRepository.getUserById(userId)
        val order = Order(
            user = user,
            createdTime = Date(),
            amount = BigDecimal.valueOf(amount),
            fromCurrency = currency,
            toCurrency = currency,
            orderType = OrderType.BUY
        )

        orderRepository.createorder(order)
    }

    fun createSellOrder(
        userId: Long,
        amount: Double,
        currency: Currency
    ) {
        val user = userRepository.getUserById(userId)
        val order = Order(
            user = user,
            createdTime = Date(),
            amount = BigDecimal.valueOf(amount),
            fromCurrency = currency,
            toCurrency = currency,
            orderType = OrderType.SELL
        )

        orderRepository.createorder(order)
    }

    fun convert(
        userId: Long,
        amount: Double,
        fromCurrency: Currency,
        toCurrency: Currency
    ) {
        val user = userRepository.getUserById(userId)
        val order = Order(
            user = user,
            createdTime = Date(),
            amount = BigDecimal.valueOf(amount),
            fromCurrency = fromCurrency,
            toCurrency = toCurrency,
            orderType = OrderType.CONVERT
        )

        orderRepository.createorder(order)
    }
}