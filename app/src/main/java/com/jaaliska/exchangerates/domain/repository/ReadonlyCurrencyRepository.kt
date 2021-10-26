package com.jaaliska.exchangerates.domain.repository

import com.jaaliska.exchangerates.domain.model.Currency

interface ReadonlyCurrencyRepository {
    suspend fun readSupportedCurrencies(codes: List<String>? = null): List<Currency>
}