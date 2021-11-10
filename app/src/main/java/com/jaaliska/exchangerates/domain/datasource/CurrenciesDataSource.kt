package com.jaaliska.exchangerates.domain.datasource

import com.jaaliska.exchangerates.domain.model.Currency
import kotlinx.coroutines.flow.Flow

interface CurrenciesDataSource {
    fun observeAll(): Flow<List<Currency>>
    fun observeFavorites(): Flow<List<Currency>>
}