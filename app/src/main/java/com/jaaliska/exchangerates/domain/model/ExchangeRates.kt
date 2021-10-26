package com.jaaliska.exchangerates.domain.model

import java.util.*

data class ExchangeRates(
    val date: Date,
    val baseCurrencyCode: String,
    val rates: List<Rate>
)
