package com.jaaliska.exchangerates.data.currency.datasource

import androidx.room.withTransaction
import com.jaaliska.exchangerates.app.persistence.ExchangeRatesDatabase
import com.jaaliska.exchangerates.data.currency.model.db.RoomCurrency
import com.jaaliska.exchangerates.domain.model.Currency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomCurrencyDataSource(
    private val db: ExchangeRatesDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun saveSupportedCurrencies(currencies: List<Currency>) {
        withContext(dispatcher) {
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
    }

    suspend fun readSupportedCurrencies(codes: List<String>? = null): List<Currency> {
        val currencies = withContext(dispatcher) {
            if (codes != null) {
                db.currencyDao().getByCodes(codes)
            } else {
                db.currencyDao().readSupportedCurrency()
            }
        }
        return currencies.map {
            Currency(
                code = it.code,
                name = it.name
            )
        }
    }

    suspend fun readFavoriteCurrencies(): List<Currency> {
        return withContext(dispatcher) {
            db.currencyDao().readFavoriteCurrencyCodes().map {
                Currency(
                    code = it.code,
                    name = it.name
                )
            }
        }
    }

    suspend fun saveFavoriteCurrencies(currencyCodes: Set<String>) {
        withContext(dispatcher) {
            db.currencyDao().setIsFavorite(currencyCodes)
        }
    }
}