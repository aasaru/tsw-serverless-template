package com.getchange

import com.getchange.orderengine.Currency
import com.getchange.orderengine.OrderEntity
import com.getchange.orderengine.OrderType
import com.getchange.tsw.OrdersRepository
import com.getchange.util.RespositoryTestRunner
import com.getchange.util.insertTestUser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.sql.Connection

@DisplayName("Order Repository")
@ExtendWith(RespositoryTestRunner::class)
class OrderRepositorySpec {

    lateinit var connection: Connection
    lateinit var ordersRepository: OrdersRepository

    @BeforeEach
    fun setUp() {
        connection = RespositoryTestRunner.connection
        ordersRepository = OrdersRepository(connection)
    }
    @Test
    fun `creates order`() {
        insertTestUser(connection)
        ordersRepository.createorder(getSampleOrderEntity())
        val query = """
            SELECT * FROM orders
        """.trimIndent()
        val resultSet = connection.createStatement().executeQuery(query)
        Assertions.assertTrue(resultSet.next())
        Assertions.assertEquals(OrderType.SELL.toString(), resultSet.getString("order_type"))
    }

    @Test
    fun `can get orders for a user`() {
        insertTestUser(connection)
        val orderEntity = getSampleOrderEntity()
        ordersRepository.createorder(orderEntity)
        ordersRepository.createorder(orderEntity)
        ordersRepository.createorder(orderEntity)
        val orders = ordersRepository.getOrdersForUser(orderEntity.userId)
        Assertions.assertEquals(3, orders.size)
    }

    private fun getSampleOrderEntity(): OrderEntity {
        val userId = 1L
        return OrderEntity(
            userId = userId,
            amount = BigDecimal.valueOf(10.0),
            orderType = OrderType.SELL,
            toCurrency = Currency.BTC,
            fromCurrency = Currency.BTC,
            createdTime = java.util.Date()
        )
    }
}