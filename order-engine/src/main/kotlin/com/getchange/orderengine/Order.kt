package com.getchange.orderengine

import java.math.BigDecimal
import java.util.Date

data class Order(
    val id: Long? = null,
    val user: User,
    val amount: BigDecimal,
    val fromCurrency: Currency,
    val toCurrency: Currency,
    val orderType: OrderType,
    val createdTime: Date
)