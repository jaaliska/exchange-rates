package com.jaaliska.exchangerates.domain.model

data class Rate(
    val currency: Currency,
    val rate: Double
)
