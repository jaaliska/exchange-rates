package com.jaaliska.exchangerates.data.rates.repository

import androidx.room.withTransaction
import com.jaaliska.exchangerates.app.persistence.ExchangeRatesDatabase
import com.jaaliska.exchangerates.data.rates.model.db.RoomExchangeRateBaseCurrency
import com.jaaliska.exchangerates.data.rates.model.db.RoomExchangeRates
import com.jaaliska.exchangerates.domain.RatesNotFoundException
import com.jaaliska.exchangerates.domain.CurrencyNotFoundException
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.Rate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import kotlin.jvm.Throws

typealias BaseCurrencyCode = String

class RoomRatesRepository(private val db: ExchangeRatesDatabase) {

    fun getDateChanges(): Flow<Map<BaseCurrencyCode, Date>> {
        return db.exchangeRateBaseCurrencyDao().getAll()
            .map {
                it.associate {
                    it.currencyCode to it.updateDate
                }
            }
    }

    @Throws(RatesNotFoundException::class)
    suspend fun getRates(
        baseCurrencyCode: String,
        currencies: List<Currency>
    ): ExchangeRates {
        val updateDate =
            db.exchangeRateBaseCurrencyDao().getByCurrencyCode(baseCurrencyCode)?.updateDate
                ?: throw RatesNotFoundException(baseCurrencyCode)

        val result =
            db.exchangesRatesDao().getByBaseCode(baseCurrencyCode, currencies.map { it.code })
        return ExchangeRates(
            date = updateDate,
            baseCurrency = Currency(
                code = baseCurrencyCode,
                name = currencies.find { it.code == baseCurrencyCode }?.name
                    ?: throw CurrencyNotFoundException(baseCurrencyCode)
            ),
            rates = result.map {
                Rate(
                    currency = currencies.find { currency ->
                        currency.code == it.currencyCode
                    } ?: throw CurrencyNotFoundException(it.currencyCode),
                    exchangeRate = it.rate
                )
            }
        )
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
                    currencyCode = exchangeRates.baseCurrency.code,
                    updateDate = exchangeRates.date
                )
            )

            db.exchangesRatesDao().insert(exchangeRates.rates.map {
                RoomExchangeRates(
                    baseCurrencyCode = exchangeRates.baseCurrency.code,
                    currencyCode = it.currency.code,
                    rate = it.exchangeRate
                )
            })
        }
    }

}