package com.getchange.tsw.platform.aws.dto

data class CreateBuySellOrderDto(
    val userId: Long,
    val amount: Double,
    val currency: String
)