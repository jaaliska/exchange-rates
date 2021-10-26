package com.jaaliska.exchangerates.domain.repository

import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.repository.exception.RatesNotFoundException

interface ReadonlyRatesRepository {
    @Throws(RatesNotFoundException::class)
    suspend fun getRates(baseCurrencyCode: String, currencyCodes: List<String>): ExchangeRates
}