package com.getchange.orderengine

import com.nhaarman.mockito_kotlin.argWhere
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Order Service")
class OrderServiceSpec {

    private lateinit var orderService: OrderService
    private lateinit var orderRepository: OrderRepository

    private val sampleUser = UserEntity(
        id = 1L,
        email = "test@example.com"
    )

    @BeforeEach
    fun setUp() {
        orderRepository = mock()
        orderService = OrderService(
            orderRepository
        )
    }

    @Test
    fun `creates sell order`() {
        val userId = 1L
        orderService.createSellOrder(
            userId = userId,
            amount = 10.0,
            currency = Currency.BTC
        )
        verify(orderRepository).createorder(argWhere {
            (it.userId == userId && it.toCurrency == Currency.BTC && it.fromCurrency == Currency.BTC)
        })
    }

    @Test
    fun `creates buy order`() {
        val userId = 1L
        orderService.createBuyOrder(
            userId = userId,
            amount = 10.0,
            currency = Currency.BTC
        )
        verify(orderRepository).createorder(argWhere {
            (it.userId == userId && it.toCurrency == Currency.BTC && it.fromCurrency == Currency.BTC)
        })
    }

    @Test
    fun `creates convert order`() {
        val userId = 1L
        orderService.convert(
            userId = userId,
            amount = 10.0,
            fromCurrency = Currency.BTC,
            toCurrency = Currency.EUR
        )
        verify(orderRepository).createorder(argWhere {
            (it.userId == userId && it.toCurrency == Currency.EUR && it.fromCurrency == Currency.BTC)
        })
    }

    @Test
    fun `lists orders for a user`() {
        val userId = 1L
        orderService.listOrdersForUser(userId)
        verify(orderRepository).getOrdersForUser(userId)
    }
}