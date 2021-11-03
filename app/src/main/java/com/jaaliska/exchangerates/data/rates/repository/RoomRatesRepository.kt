package com.jaaliska.exchangerates.data.rates.repository

import androidx.room.withTransaction
import com.jaaliska.exchangerates.app.persistence.ExchangeRatesDatabase
import com.jaaliska.exchangerates.data.rates.model.db.RoomExchangeRateBaseCurrency
import com.jaaliska.exchangerates.data.rates.model.db.RoomExchangeRates
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.Rate
import com.jaaliska.exchangerates.domain.RatesNotFoundException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class RoomRatesRepository(private val db: ExchangeRatesDatabase) {

    suspend fun getRates(
        baseCurrencyCode: String,
        currencyCodes: List<String>
    ): Flow<ExchangeRates> {
        val baseCode = if (baseCurrencyCode == "") {
            db.currencyDao().readFavoriteCurrencyCodes()[0]
        } else baseCurrencyCode

        val comparator = { old: List<RoomExchangeRates>, new: List<RoomExchangeRates> ->
            old == new
        }

        val result = db.exchangesRatesDao().getByBaseCode(baseCode, currencyCodes)
            .distinctUntilChanged(comparator)
        return result.map {
            ExchangeRates(
                date = db.exchangeRateBaseCurrencyDao().getByCurrencyCode(baseCode)?.updateDate
                    ?: throw RatesNotFoundException(baseCode),
                baseCurrencyCode = baseCode,
                rates = it.map {
                    Rate(
                        currencyCode = it.currencyCode,
                        exchangeRate = it.rate
                    )
                }
            )
        }
    }

    suspend fun deleteAllRates() {
        db.withTransaction {
            db.exchangeRateBaseCurrencyDao().deleteAll()
            db.exchangesRatesDao().deleteAll()
        }
    }

    suspend fun saveRates(exchangeRates: ExchangeRates) {
        db.withTransaction {
            db.exchangeRateBaseCurrencyDao().insert(
                RoomExchangeRateBaseCurrency(
                    currencyCode = exchangeRates.baseCurrencyCode,
                    updateDate = exchangeRates.date
                )
            )

            db.exchangesRatesDao().insert(exchangeRates.rates.map {
                RoomExchangeRates(
                    baseCurrencyCode = exchangeRates.baseCurrencyCode,
                    currencyCode = it.currencyCode,
                    rate = it.exchangeRate
                )
            })
        }
    }

}