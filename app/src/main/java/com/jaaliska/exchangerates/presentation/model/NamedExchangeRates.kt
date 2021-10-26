package com.jaaliska.exchangerates.presentation.model

import com.jaaliska.exchangerates.domain.model.Currency
import java.util.*

data class NamedExchangeRates(
    val date: Date,
    val baseCurrency: Currency,
    val rates: List<Pair<Currency, Double>>
)
