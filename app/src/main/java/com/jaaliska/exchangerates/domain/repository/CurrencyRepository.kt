package com.jaaliska.exchangerates.domain.repository

import com.jaaliska.exchangerates.domain.model.Currency

interface CurrencyRepository: ReadonlyCurrencyRepository {
    suspend fun saveSupportedCurrencies(currencies: List<Currency>)
}