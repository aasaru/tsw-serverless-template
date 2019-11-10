package com.getchange.orderengine

import java.math.BigDecimal
import java.util.Date

data class OrderEntity(
    val id: Long? = null,
    val userId: Long,
    val amount: BigDecimal,
    val fromCurrency: Currency,
    val toCurrency: Currency,
    val orderType: OrderType,
    val createdTime: Date
)