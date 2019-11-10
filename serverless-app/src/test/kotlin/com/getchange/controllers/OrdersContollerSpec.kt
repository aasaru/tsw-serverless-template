package com.getchange.controllers

import com.getchange.orderengine.Currency
import com.getchange.orderengine.OrderService
import com.getchange.tsw.controllers.OrdersController
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Orders Controller")
class OrdersContollerSpec {
    lateinit var orderService: OrderService
    lateinit var ordersController: OrdersController

    @BeforeEach
    fun setUp() {
        orderService = mock()
        ordersController = OrdersController((orderService))
    }

    @Test
    fun `POST buy returns 201 CREATED`() {
        val response = ordersController.buy(
            userId = 1L,
            currency = "BTC",
            amount = 0.001
        )
        verify(orderService).createBuyOrder(
            userId = 1L,
            currency = Currency.BTC,
            amount = 0.001
        )
        Assertions.assertEquals(201, response.statusCode)
    }

    @Test
    fun `POST sell returns 201 CREATED`() {
        val response = ordersController.sell(
            userId = 1L,
            currency = "BTC",
            amount = 0.001
        )

        verify(orderService).createSellOrder(
            userId = 1L,
            currency = Currency.BTC,
            amount = 0.001
        )

        Assertions.assertEquals(201, response.statusCode)
    }

    @Test
    fun `POST convert returns 201 CREATED`() {
        val response = ordersController.convert(
            userId = 1L,
            from = "BTC",
            to = "EUR",
            amount = 0.001
        )
        verify(orderService).convert(
            userId = 1L,
            fromCurrency = Currency.BTC,
            toCurrency = Currency.EUR,
            amount = 0.001
        )

        Assertions.assertEquals(201, response.statusCode)
    }

    @Test
    fun `GET user orders returns 201 created`() {
        val userId = 1L
        val response = ordersController.getUserOrders(userId)
        verify(orderService).listOrdersForUser(userId)
        Assertions.assertEquals(200, response.statusCode)
    }
}