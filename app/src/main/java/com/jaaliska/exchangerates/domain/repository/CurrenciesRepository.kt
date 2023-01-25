package com.jaaliska.exchangerates.domain.repository

import com.jaaliska.exchangerates.domain.model.Currency

interface CurrenciesRepository {
    suspend fun getFavorite(): List<Currency>
    suspend fun getSupported(forceUpdate: Boolean): List<Currency>
}