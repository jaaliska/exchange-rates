package com.jaaliska.exchangerates.const

import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.Rate
import java.util.*

object TestData {

    val listCodes = listOf("AMD", "ALL", "AED", "AFN")
    val listCurrencies = mutableListOf(
        Currency("AMD", "Armenian dram"),
        Currency("ALL", "Albanian lek"),
        Currency("AED", "United Arab Emirates dirham"),
        Currency("AFN", "Afghan afghani")
    )

    val listCurrenciesWithoutBaseCurrency = mutableListOf(
        Currency("ALL", "Albanian lek"),
        Currency("AED", "United Arab Emirates dirham"),
        Currency("AFN", "Afghan afghani")
    )


    val exchangeRatesByAMD = ExchangeRates(
        Date(987654321),
        Currency("AMD", "Armenian dram"),
        listOf(
            Rate(
                Currency("AED", "United Arab Emirates dirham"),
                0.00774379
            ),
            Rate(
                Currency("AFN", "Afghan afghani"),
                0.19273443
            ),
            Rate(
                Currency("ALL", "Albanian lek"),
                0.22516653
            )
        )
    )

    val exchangeRatesByAED = ExchangeRates(
        Date(987654321),
        Currency("AED", "United Arab Emirates dirham"),
        listOf(
            Rate(
                Currency("AMD", "Armenian dram"),
                0.00564984
            ),
            Rate(
                Currency("AFN", "Afghan afghani"),
                0.19273443
            ),
            Rate(
                Currency("ALL", "Albanian lek"),
                0.22516653
            )
        )
    )

    val exchangeRatesByAFN = ExchangeRates(
        Date(987654321),
        Currency("AFN", "Afghan afghani"),
        listOf(
            Rate(
                Currency("AMD", "Armenian dram"),
                0.00564984
            ),
            Rate(
                Currency("AED", "United Arab Emirates dirham"),
                0.19273443
            ),
            Rate(
                Currency("ALL", "Albanian lek"),
                0.22516653
            )
        )
    )

    val exchangeRatesByALL = ExchangeRates(
        Date(987654321),
        Currency("ALL", "Albanian lek"),
        listOf(
            Rate(
                Currency("AMD", "Armenian dram"),
                0.00564984
            ),
            Rate(
                Currency("AFN", "Afghan afghani"),
                0.19273443
            ),
            Rate(
                Currency("AED", "United Arab Emirates dirham"),
                0.22516653
            )
        )
    )
}