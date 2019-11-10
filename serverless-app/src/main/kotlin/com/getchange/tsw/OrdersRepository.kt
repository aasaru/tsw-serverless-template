package com.getchange.tsw

import com.getchange.orderengine.*
import java.sql.Connection

class OrdersRepository(
    private val connection: Connection
) : OrderRepository {
    override fun createorder(order: OrderEntity) {
        val query = """
            INSERT INTO orders (user_id, amount, from_currency, to_currency, order_type)
            VALUES (?, ?, ?, ?, ?)
        """.trimIndent()
        val statement = connection.prepareStatement(query)
        statement.setLong(1, order.userId)
        statement.setBigDecimal(2, order.amount)
        statement.setString(3, order.fromCurrency.toString())
        statement.setString(4, order.toCurrency.toString())
        statement.setString(5, order.orderType.toString())
        statement.executeUpdate()
    }

    override fun getOrdersForUser(userId: Long): List<OrderEntity> {
        val query = """
            SELECT * FROM orders WHERE user_id=?
        """.trimIndent()
        val statement = connection.prepareStatement(query)
        statement.setLong(1, userId)
        val resultSet = statement.executeQuery()
        val ordersList = ArrayList<OrderEntity>()
        while (resultSet.next()) {
            val o = OrderEntity(
                id = resultSet.getLong("id"),
                amount = resultSet.getBigDecimal("amount"),
                userId = resultSet.getLong("user_id"),
                createdTime = resultSet.getDate("created_time"),
                fromCurrency = Currency.valueOf(resultSet.getString("from_currency")),
                toCurrency = Currency.valueOf(resultSet.getString("to_currency")),
                orderType = OrderType.valueOf(resultSet.getString("order_type"))
            )
            ordersList.add(o)
        }
        return ordersList
    }
}