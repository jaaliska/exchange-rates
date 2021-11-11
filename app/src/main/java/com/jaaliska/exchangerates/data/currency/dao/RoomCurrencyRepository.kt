package com.jaaliska.exchangerates.data.currency.dao

import androidx.room.withTransaction
import com.jaaliska.exchangerates.app.persistence.ExchangeRatesDatabase
import com.jaaliska.exchangerates.domain.model.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomCurrencyRepository(private val db: ExchangeRatesDatabase) {

    suspend fun saveSupportedCurrencies(currencies: List<Currency>) {
        db.withTransaction {
            db.currencyDao().deleteAll()
            val list = currencies.map { currency -> RoomCurrency(currency) }
            db.currencyDao().insert(list)
        }
    }

    fun readSupportedCurrencies(): Flow<List<Currency>> {
        return db.currencyDao().readAll().map { it.map(RoomCurrency::toDomain) }
    }

    fun readFavoriteCurrencies(): Flow<List<Currency>> {
        return db.currencyDao().readFavorites().map { it.map(RoomCurrency::toDomain) }
    }

    suspend fun markAsFavorite(currency: Currency, isFavorite: Boolean) {
        db.currencyDao().setIsFavorite(code = currency.code, isFavorite = isFavorite)
    }
}