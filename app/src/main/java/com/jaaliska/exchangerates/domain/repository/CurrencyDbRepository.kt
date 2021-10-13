package com.jaaliska.exchangerates.domain.repository

import com.jaaliska.exchangerates.domain.model.ExchangeRates

interface CurrencyDbRepository {

    suspend fun getExchangeRates(baseCurrencyCode: String): ExchangeRates?
    suspend fun saveExchangeRates(exchangeRates: ExchangeRates)

}