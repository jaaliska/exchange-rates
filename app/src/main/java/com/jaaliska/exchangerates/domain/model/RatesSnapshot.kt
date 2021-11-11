package com.jaaliska.exchangerates.domain.model

import java.util.*

data class RatesSnapshot(
    val date: Date,
    val baseCurrency: Currency,
    val rates: List<Rate>
) {
    data class Rate(
        val currency: Currency,
        val rate: Double
    )
}
