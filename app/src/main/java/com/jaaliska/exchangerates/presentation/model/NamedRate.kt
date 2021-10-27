package com.jaaliska.exchangerates.presentation.model

data class NamedRate(
    val currencyCode: String,
    val currencyName: String,
    val exchangeRate: Double
)
