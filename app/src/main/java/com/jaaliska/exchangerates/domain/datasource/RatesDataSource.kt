package com.jaaliska.exchangerates.domain.datasource

import com.jaaliska.exchangerates.domain.model.RatesSnapshot
import kotlinx.coroutines.flow.Flow

interface RatesDataSource {
    fun observe(): Flow<RatesSnapshot?>
    suspend fun refresh()
}