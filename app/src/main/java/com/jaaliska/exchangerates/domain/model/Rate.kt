package com.jaaliska.exchangerates.domain.model


data class Rate(
    val currencyCode: String,
    val exchangeRate: Double
)
