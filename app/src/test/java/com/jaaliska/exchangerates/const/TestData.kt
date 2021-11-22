package com.jaaliska.exchangerates.const

import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.Rate
import java.util.*

object TestData {

    val setCodes = setOf("AMD", "ALL", "AED", "AFN")
    val listCurrencies = mutableListOf(
        Currency(code = "AMD", name = "Armenian dram"),
        Currency(code = "ALL", name = "Albanian lek"),
        Currency(code = "AED", name = "United Arab Emirates dirham"),
        Currency(code = "AFN", name = "Afghan afghani")
    )

    val listCurrenciesWithoutBaseCurrency = mutableListOf(
        Currency(code ="ALL", name = "Albanian lek"),
        Currency(code ="AED", name = "United Arab Emirates dirham"),
        Currency(code ="AFN", name = "Afghan afghani")
    )


    val exchangeRatesByAMD = ExchangeRates(
        Date(987654321),
        Currency(code = "AMD", name = "Armenian dram"),
        listOf(
            Rate(
                Currency(code = "AED", name = "United Arab Emirates dirham"),
                0.00774379
            ),
            Rate(
                Currency(code = "AFN", name = "Afghan afghani"),
                0.19273443
            ),
            Rate(
                Currency(code = "ALL", name = "Albanian lek"),
                0.22516653
            )
        )
    )

    val exchangeRatesByAED = ExchangeRates(
        Date(987654321),
        Currency(code = "AED", name =  "United Arab Emirates dirham"),
        listOf(
            Rate(
                Currency(code = "AMD", name = "Armenian dram"),
                0.00564984
            ),
            Rate(
                Currency(code = "AFN", name = "Afghan afghani"),
                0.19273443
            ),
            Rate(
                Currency(code = "ALL", name = "Albanian lek"),
                0.22516653
            )
        )
    )

    val exchangeRatesByAFN = ExchangeRates(
        Date(987654321),
        Currency(code = "AFN", name = "Afghan afghani"),
        listOf(
            Rate(
                Currency(code = "AMD", name = "Armenian dram"),
                0.00564984
            ),
            Rate(
                Currency(code = "AED", name = "United Arab Emirates dirham"),
                0.19273443
            ),
            Rate(
                Currency(code = "ALL", name = "Albanian lek"),
                0.22516653
            )
        )
    )

    val exchangeRatesByALL = ExchangeRates(
        Date(987654321),
        Currency(code = "ALL", name = "Albanian lek"),
        listOf(
            Rate(
                Currency(code = "AMD", name = "Armenian dram"),
                0.00564984
            ),
            Rate(
                Currency(code = "AFN", name = "Afghan afghani"),
                0.19273443
            ),
            Rate(
                Currency(code = "AED", name = "United Arab Emirates dirham"),
                0.22516653
            )
        )
    )
}