package com.jaaliska.exchangerates.data.datasource

import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.data.rates.repository.RetrofitRatesRepository
import com.jaaliska.exchangerates.data.rates.repository.RoomRatesRepository
import com.jaaliska.exchangerates.domain.RatesNotFoundException
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.datasource.RatesDataSource
import com.jaaliska.exchangerates.presentation.service.AlarmService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class RatesDataSourceImpl(
    private val localRatesRepository: RoomRatesRepository,
    private val localCurrencyRepository: RoomCurrencyRepository,
    private val remoteRatesRepository: RetrofitRatesRepository,
    private val alarmService: AlarmService
) : RatesDataSource {

    override suspend fun get(
        baseCurrencyCode: String,
        currencies: List<Currency>?
    ): ExchangeRates {
        val favorites = currencies ?: localCurrencyRepository.readFavoriteCurrencies()
        return try {
            localRatesRepository.getRates(baseCurrencyCode, favorites)
        } catch (ex: RatesNotFoundException) {
            refresh()
            return localRatesRepository.getRates(baseCurrencyCode, favorites)
        }
    }

    override suspend fun getNamedRates(baseCurrencyCode: String): ExchangeRates {
        val favorites = localCurrencyRepository.readFavoriteCurrencies()
        val codesToLoad = favorites.map { it.code }.toMutableList()
        if (!codesToLoad.contains(baseCurrencyCode)) {
            codesToLoad.add(baseCurrencyCode)
        }

        val currencies =
            localCurrencyRepository.readSupportedCurrencies(codesToLoad).associateBy { it.code }
        val rates = get(baseCurrencyCode, favorites)
        val getCurrency = { code: String -> currencies[code] ?: Currency("", code) }

        return ExchangeRates(
            date = rates.date,
            baseCurrency = getCurrency(rates.baseCurrency.code),
            rates = rates.rates
        )
    }

    override suspend fun refresh() {
        val favorites = localCurrencyRepository.readFavoriteCurrencies()
        localRatesRepository.deleteAllRates()
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
        val rates = remoteRatesRepository.getRates(baseCurrency, currencyToLoad)
        localRatesRepository.saveRates(rates)
    }
}