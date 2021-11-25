package com.jaaliska.exchangerates.data.currency.persistence

import com.jaaliska.exchangerates.domain.model.Currency
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun saveSupportedCurrencies(currencies: List<Currency>)
    fun readSupportedCurrencies(): Flow<List<Currency>>
    fun readFavoriteCurrencies(): Flow<List<Currency>>
    suspend fun markAsFavorite(currency: Currency, isFavorite: Boolean)
}