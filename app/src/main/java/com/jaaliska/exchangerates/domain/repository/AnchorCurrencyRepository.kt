package com.jaaliska.exchangerates.domain.repository

import kotlinx.coroutines.flow.Flow

interface AnchorCurrencyRepository {
    fun getAnchorCurrencyCode(): Flow<String>
    fun setAnchorCurrencyCode(code: String)
}