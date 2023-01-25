package com.jaaliska.exchangerates.data.repository

import com.jaaliska.exchangerates.data.currency.datasource.RoomCurrencyDataSource
import com.jaaliska.exchangerates.data.rates.datasource.RetrofitRatesDataSource
import com.jaaliska.exchangerates.data.rates.datasource.RoomRatesDataSource
import com.jaaliska.exchangerates.domain.RatesNotFoundException
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.repository.RatesRepository
import com.jaaliska.exchangerates.presentation.service.AlarmService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class RatesRepositoryImpl(
    private val localRatesDataSource: RoomRatesDataSource,
    private val localCurrencyDataSource: RoomCurrencyDataSource,
    private val remoteRatesDataSource: RetrofitRatesDataSource,
    private val alarmService: AlarmService
) : RatesRepository {

    override suspend fun get(
        baseCurrencyCode: String,
        currencies: List<Currency>?
    ): ExchangeRates {
        val favorites = currencies ?: localCurrencyDataSource.readFavoriteCurrencies()
        return try {
            localRatesDataSource.getRates(baseCurrencyCode, favorites)
        } catch (ex: RatesNotFoundException) {
            refresh()
            return localRatesDataSource.getRates(baseCurrencyCode, favorites)
        }
    }

    override suspend fun getNamedRates(baseCurrencyCode: String): ExchangeRates {
        val favorites = localCurrencyDataSource.readFavoriteCurrencies()
        val codesToLoad = favorites.map { it.code }.toMutableList()
        if (!codesToLoad.contains(baseCurrencyCode)) {
            codesToLoad.add(baseCurrencyCode)
        }

        val currencies =
            localCurrencyDataSource.readSupportedCurrencies(codesToLoad).associateBy { it.code }
        val rates = get(baseCurrencyCode, favorites)
        val getCurrency = { code: String -> currencies[code] ?: Currency("", code) }

        return ExchangeRates(
            date = rates.date,
            baseCurrency = getCurrency(rates.baseCurrency.code),
            rates = rates.rates
        )
    }

    override suspend fun refresh() {
        val favorites = localCurrencyDataSource.readFavoriteCurrencies()
        localRatesDataSource.deleteAllRates()
        coroutineScope {
            val tasks = mutableListOf<Deferred<Unit>>()
            for (baseCurrency in favorites) {
                val currencyToLoad = favorites.filter { it.code != baseCurrency.code }
                tasks.add(async {
                    updateRatesForBaseCurrency(baseCurrency, currencyToLoad)
                })
            }
            awaitAll(*tasks.toTypedArray())
        }
        alarmService.startAlarm()
    }

    private suspend fun updateRatesForBaseCurrency(
        baseCurrency: Currency,
        currencyToLoad: List<Currency>
    ) {
        val rates = remoteRatesDataSource.getRates(baseCurrency, currencyToLoad)
        localRatesDataSource.saveRates(rates)
    }
}