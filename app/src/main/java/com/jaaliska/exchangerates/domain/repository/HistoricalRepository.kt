package com.jaaliska.exchangerates.domain.repository

import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates

interface HistoricalRepository {
    suspend fun getYearHistoryForCurrency(
        year: Int,
        baseCurrency: Currency,
        currenciesRateFor: Currency
    ): List<ExchangeRates>
}