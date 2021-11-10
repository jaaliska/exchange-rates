package com.jaaliska.exchangerates.domain.datasource

import com.jaaliska.exchangerates.domain.model.ExchangeRates
import kotlinx.coroutines.flow.Flow

interface RatesDataSource {
    fun observe(): Flow<ExchangeRates?>
    suspend fun refresh()
}