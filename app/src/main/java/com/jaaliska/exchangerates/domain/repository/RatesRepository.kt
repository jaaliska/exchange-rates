package com.jaaliska.exchangerates.domain.repository

import com.jaaliska.exchangerates.domain.model.ExchangeRates

interface RatesRepository : ReadonlyRatesRepository {
    suspend fun deleteAllRates()
    suspend fun saveRates(exchangeRates: ExchangeRates)
}