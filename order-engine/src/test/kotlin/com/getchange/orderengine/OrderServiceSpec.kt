package com.getchange.orderengine

import com.nhaarman.mockito_kotlin.argWhere
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Order Service")
class OrderServiceSpec {

    lateinit var orderService: OrderService
    lateinit var orderRepository: OrderRepository
    lateinit var userRepository: UserRepository

    val sampleUser = User(
        id = 1L,
        email = "test@example.com"
    )

    @BeforeEach
    fun setUp() {
        orderRepository = mock()
        userRepository = mock()
        orderService = OrderService(
            orderRepository,
            userRepository
        )
    }

    @Test
    fun `loads the user for creating order`() {
            val userId = 1L
            whenever(userRepository.getUserById(userId)).thenReturn(sampleUser)
            orderService.createSellOrder(
                userId = userId,
                amount = 10.0,
                currency = Currency.BTC
            )
            verify(userRepository).getUserById(userId)
    }

    @Test
    fun `creates order`() {
        val userId = 1L
        whenever(userRepository.getUserById(userId)).thenReturn(sampleUser)
        orderService.createSellOrder(
            userId = userId,
            amount = 10.0,
            currency = Currency.BTC
        )
        verify(orderRepository).createorder(argWhere {
            (it.user.id == userId && it.toCurrency == Currency.BTC && it.toCurrency == Currency.BTC)
        })
    }
}