package com.jaaliska.exchangerates.data.rates.repository

import androidx.room.withTransaction
import com.jaaliska.exchangerates.app.persistence.ExchangeRatesDatabase
import com.jaaliska.exchangerates.data.rates.model.db.RoomExchangeRateBaseCurrency
import com.jaaliska.exchangerates.data.rates.model.db.RoomExchangeRates
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.Rate
import com.jaaliska.exchangerates.domain.RatesNotFoundException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomRatesRepository(private val db: ExchangeRatesDatabase) {

    suspend fun getRates(
        baseCurrencyCode: String,
        currencyCodes: List<String>
    ): Flow<ExchangeRates> {
        val updateDate =
            db.exchangeRateBaseCurrencyDao().getByCurrencyCode(baseCurrencyCode)?.updateDate
                ?: throw RatesNotFoundException(baseCurrencyCode)

        val result = db.exchangesRatesDao().getByBaseCode(baseCurrencyCode, currencyCodes)
        return result.map {
            ExchangeRates(
                date = updateDate,
                baseCurrencyCode = baseCurrencyCode,
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