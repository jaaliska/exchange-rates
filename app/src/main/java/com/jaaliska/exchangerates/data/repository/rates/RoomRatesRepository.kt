package com.jaaliska.exchangerates.data.repository.rates

import androidx.room.withTransaction
import com.jaaliska.exchangerates.app.persistence.ExchangeRatesDatabase
import com.jaaliska.exchangerates.data.model.database.RoomExchangeRateBaseCurrency
import com.jaaliska.exchangerates.data.model.database.RoomExchangeRates
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.Rate
import com.jaaliska.exchangerates.domain.repository.RatesRepository
import com.jaaliska.exchangerates.domain.repository.exception.RatesNotFoundException

class RoomRatesRepository(private val db: ExchangeRatesDatabase) : RatesRepository {

    override suspend fun getRates(
        baseCurrencyCode: String,
        currencyCodes: List<String>
    ): ExchangeRates {
        val updateDate = db.exchangeRateBaseCurrencyDao().getByCurrencyCode(baseCurrencyCode)?.updateDate
            ?: throw RatesNotFoundException(baseCurrencyCode)

        val result = db.exchangesRatesDao().getByBaseCode(baseCurrencyCode, currencyCodes)

        return ExchangeRates(
            date = updateDate,
            baseCurrencyCode = baseCurrencyCode,
            rates = result.map {
                Rate(
                    currencyCode = it.currencyCode,
                    exchangeRate = it.rate
                )
            }
        )
    }

    override suspend fun deleteAllRates() {
        db.withTransaction {
            db.exchangeRateBaseCurrencyDao().deleteAll()
            db.exchangesRatesDao().deleteAll()
        }
    }

    override suspend fun saveRates(exchangeRates: ExchangeRates) {
        db.withTransaction {
            db.exchangeRateBaseCurrencyDao().insert(
                RoomExchangeRateBaseCurrency(exchangeRates.baseCurrencyCode, exchangeRates.date)
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