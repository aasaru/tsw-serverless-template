package com.getchange.tsw.platform.aws.dto

data class CreateConvertOrderDto(
    val userId: Long,
    val amount: Double,
    val fromCurrency: String,
    val toCurrency: String
)