package com.jaaliska.exchangerates.domain.datasource

import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates

interface RatesDataSource {

    suspend fun get(
        baseCurrencyCode: String,
        currencies: List<Currency>? = null
    ): ExchangeRates

    suspend fun getNamedRates(baseCurrencyCode: String): ExchangeRates

    suspend fun refresh()
}