package com.jaaliska.exchangerates.const

import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.Rate
import com.jaaliska.exchangerates.presentation.ui.currencyChoice.BaseCurrencyChoiceDialogViewModel.SelectableItem
import java.util.*

object TestData {

    val setCodes = setOf("AMD", "ALL", "AED", "AFN")
    val listCurrencies = listOf(
        Currency(code = "AMD", name = "1"),
        Currency(code = "ALL", name = "2"),
        Currency(code = "AED", name = "3"),
        Currency(code = "AFN", name = "4")
    )

    val listCurrenciesWithoutBaseCurrency = listOf(
        Currency(code = "ALL", name = "2"),
        Currency(code = "AED", name = "3"),
        Currency(code = "AFN", name = "4")
    )

    val listCurrencyItemsAllFalse = listOf(
        SelectableItem(title = "AMD", subtitle = "1", isSelected = false),
        SelectableItem(title = "ALL", subtitle = "2", isSelected = false),
        SelectableItem(title = "AED", subtitle = "3", isSelected = false),
        SelectableItem(title = "AFN", subtitle = "4", isSelected = false)
    )

    val listCurrencyItemsAllTrue = listOf(
        SelectableItem(title = "AMD", subtitle = "1", isSelected = true),
        SelectableItem(title = "ALL", subtitle = "2", isSelected = true),
        SelectableItem(title = "AED", subtitle = "3", isSelected = true),
        SelectableItem(title = "AFN", subtitle = "4", isSelected = true)
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

    val exchangeRatesByAED = ExchangeRates(
        Date(987654321),
        Currency(code = "AED", name = "3"),
        listOf(
            Rate(
                Currency(code = "AMD", name = "1"),
                0.00564984
            ),
            Rate(
                Currency(code = "ALL", name = "2"),
                0.22516653
            ),
            Rate(
                Currency(code = "AFN", name = "4"),
                0.19273443
            )
        )
    )

    val exchangeRatesByAFN = ExchangeRates(
        Date(987654321),
        Currency(code = "AFN", name = "4"),
        listOf(
            Rate(
                Currency(code = "AMD", name = "1"),
                0.00564984
            ),
            Rate(
                Currency(code = "ALL", name = "2"),
                0.22516653
            ),
            Rate(
                Currency(code = "AED", name = "3"),
                0.19273443
            )
        )
    )

    val exchangeRatesByALL = ExchangeRates(
        Date(987654321),
        Currency(code = "ALL", name = "2"),
        listOf(
            Rate(
                Currency(code = "AMD", name = "1"),
                0.00564984
            ),
            Rate(
                Currency(code = "AED", name = "3"),
                0.22516653
            ),
            Rate(
                Currency(code = "AFN", name = "4"),
                0.19273443
            )
        )
    )
}