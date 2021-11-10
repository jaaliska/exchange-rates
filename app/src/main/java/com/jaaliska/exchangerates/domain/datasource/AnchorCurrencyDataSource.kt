package com.jaaliska.exchangerates.domain.datasource

import kotlinx.coroutines.flow.Flow

interface AnchorCurrencyDataSource {
    fun observe() : Flow<String>
}