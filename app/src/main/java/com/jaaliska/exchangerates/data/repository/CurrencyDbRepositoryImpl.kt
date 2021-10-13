package com.jaaliska.exchangerates.data.repository

import androidx.room.withTransaction
import com.jaaliska.exchangerates.app.persistence.ExchangeRatesDatabase
import com.jaaliska.exchangerates.data.model.database.BaseCurrencyEntity
import com.jaaliska.exchangerates.data.model.database.RateEntity
import com.jaaliska.exchangerates.domain.model.CurrencyDetails
import com.jaaliska.exchangerates.domain.model.CurrencyExchangeRate
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.repository.CurrencyDbRepository

class CurrencyDbRepositoryImpl(
    private val db: ExchangeRatesDatabase
) : CurrencyDbRepository {

    override suspend fun getExchangeRates(baseCurrencyCode: String): ExchangeRates? {
        val baseCurrencyEntity = db.baseCurrencyDao().getByCode(baseCurrencyCode) ?: return null
        val rateEntities = db.rateEntityDao().getByBaseCode(baseCurrencyCode)
        return ExchangeRates(
            date = baseCurrencyEntity.date,
            baseCurrency = CurrencyDetails(
                code = baseCurrencyEntity.code,
                name = baseCurrencyEntity.name
            ),
            rates = rateEntities.map {
                CurrencyExchangeRate(
                    currencyName = it.name,
                    currencyCode = it.code,
                    exchangeRate = it.rate
                )
            }
        )
    }

    override suspend fun saveExchangeRates(exchangeRates: ExchangeRates) {
        db.withTransaction {
            db.baseCurrencyDao().deleteAll()
            db.baseCurrencyDao().insert(
                BaseCurrencyEntity(
                    code = exchangeRates.baseCurrency.code,
                    date = exchangeRates.date,
                    name = exchangeRates.baseCurrency.name
                )
            )
            db.rateEntityDao().deleteAll()
            db.rateEntityDao().insert(exchangeRates.rates.map {
                RateEntity(
                    baseCode = exchangeRates.baseCurrency.code,
                    code = it.currencyCode,
                    name = it.currencyName,
                    rate = it.exchangeRate
                )
            })
        }
    }
}