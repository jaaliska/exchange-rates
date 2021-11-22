package com.jaaliska.exchangerates.data.currency.repository

import androidx.room.withTransaction
import com.jaaliska.exchangerates.app.persistence.ExchangeRatesDatabase
import com.jaaliska.exchangerates.data.currency.model.db.RoomCurrency
import com.jaaliska.exchangerates.domain.model.Currency

class RoomCurrencyRepository(
    private val db: ExchangeRatesDatabase
) {

    suspend fun saveSupportedCurrencies(currencies: List<Currency>) {
        db.withTransaction {
            val favoriteCurrencies = db.currencyDao().readFavoriteCurrencyCodes()
            db.currencyDao().deleteAll()
            val list = currencies.map {
                RoomCurrency(
                    code = it.code,
                    name = it.name,
                    isFavorite = favoriteCurrencies.map { it.code }.contains(it.code)
                )
            }
            db.currencyDao().insert(list)
        }
    }

    suspend fun readSupportedCurrencies(codes: List<String>? = null): List<Currency> {
        val currencies = if (codes != null) {
            db.currencyDao().getByCodes(codes)
        } else {
            db.currencyDao().readSupportedCurrency()
        }
        return currencies.map {
            Currency(
                code = it.code,
                name = it.name
            )
        }
    }

    suspend fun readFavoriteCurrencies(): List<Currency> {
        return db.currencyDao().readFavoriteCurrencyCodes().map {
            Currency(
                code = it.code,
                name = it.name
            )
        }
    }

    suspend fun saveFavoriteCurrencies(currencyCodes: Set<String>) {
        db.currencyDao().setIsFavorite(currencyCodes)
    }
}