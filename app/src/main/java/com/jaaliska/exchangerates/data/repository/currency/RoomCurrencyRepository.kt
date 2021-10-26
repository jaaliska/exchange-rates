package com.jaaliska.exchangerates.data.repository.currency

import androidx.room.withTransaction
import com.jaaliska.exchangerates.app.persistence.ExchangeRatesDatabase
import com.jaaliska.exchangerates.data.model.database.RoomCurrency
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.repository.CurrencyRepository
import com.jaaliska.exchangerates.domain.repository.FavoriteCurrenciesRepository

class RoomCurrencyRepository(
    private val db: ExchangeRatesDatabase
) : CurrencyRepository, FavoriteCurrenciesRepository {

    override suspend fun saveSupportedCurrencies(currencies: List<Currency>) {
        db.withTransaction {
            val favoriteCurrencies = db.currencyDao().readFavoriteCurrencyCodes()
            db.currencyDao().deleteAll()
            val list = currencies.map {
                RoomCurrency(
                    code = it.code,
                    name = it.name,
                    isFavorite = favoriteCurrencies.contains(it.code)
                )
            }
            db.currencyDao().insert(list)
        }
    }

    override suspend fun readSupportedCurrencies(codes: List<String>?): List<Currency> {
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

    override suspend fun readFavoriteCurrencies(): List<String> {
        return db.currencyDao().readFavoriteCurrencyCodes()
    }

    override suspend fun saveFavoriteCurrencies(currencyCodes: List<String>) {
        db.currencyDao().setIsFavorite(currencyCodes)
    }
}