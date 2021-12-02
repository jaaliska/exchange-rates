package com.jaaliska.exchangerates.domain.datasource

import com.jaaliska.exchangerates.domain.model.Currency

interface CurrenciesDataSource {
    suspend fun getFavorite(): List<Currency>
    suspend fun getSupported(forceUpdate: Boolean): List<Currency>
}