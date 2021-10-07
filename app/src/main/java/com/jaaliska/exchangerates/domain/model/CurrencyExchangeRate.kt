package com.jaaliska.exchangerates.domain.model


data class CurrencyExchangeRate(
    val currencyName: String,
    val currencyCode: String,
    val exchangeRate: Double
)
