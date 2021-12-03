package com.jaaliska.exchangerates.const

import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.Rate
import java.util.*

object TestData {

    val setOfCurrencyCodes = setOf("AMD", "ALL", "AED", "AFN")
    val listCurrencies = listOf(
        Currency(code = "AMD", name = "1"),
        Currency(code = "ALL", name = "2"),
        Currency(code = "AED", name = "3"),
        Currency(code = "AFN", name = "4")
    )

    val exchangeRatesByAMD = ExchangeRates(
        Date(987654321),
        Currency(code = "AMD", name = "1"),
        listOf(
            Rate(
                Currency(code = "ALL", name = "2"),
                0.22516653
            ),
            Rate(
                Currency(code = "AED", name = "3"),
                0.00774379
            ),
            Rate(
                Currency(code = "AFN", name = "4"),
                0.19273443
            )
        )
    )
}