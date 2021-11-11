package com.jaaliska.exchangerates.domain.model

import java.util.*

data class ExchangeRates(
    val date: Date,
    val baseCurrency: Currency,
    val rates: List<Rate>
)
