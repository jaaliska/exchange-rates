package com.jaaliska.exchangerates.data.rates

import com.jaaliska.exchangerates.data.anchor_currency.SharedPrefAnchorCurrencyRepository
import com.jaaliska.exchangerates.data.core.registerHooks
import com.jaaliska.exchangerates.data.currency.dao.RoomCurrencyRepository
import com.jaaliska.exchangerates.data.rates.api.RetrofitRatesRepository
import com.jaaliska.exchangerates.data.rates.dao.RoomRatesRepository
import com.jaaliska.exchangerates.domain.datasource.RatesDataSource
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.presentation.service.AlarmService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.*

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class MediatorRatesDataSource(
    private val localRatesRepository: RoomRatesRepository,
    private val remoteRatesRepository: RetrofitRatesRepository,
    private val localCurrencyRepository: RoomCurrencyRepository,
    private val anchorCurrencyRepository: SharedPrefAnchorCurrencyRepository,
    private val alarmService: AlarmService,
    coroutineScope: CoroutineScope
) : RatesDataSource {

    init {
        with(coroutineScope) {
            registerHooks(
                anchorCurrencyRepository.observe(),
                localCurrencyRepository.readFavoriteCurrencies(),
                action = ::refresh
            )
        }
    }

    override fun observe(): Flow<ExchangeRates?> {
        return anchorCurrencyRepository.observe().flatMapLatest { anchorCurrencyCode ->
            localCurrencyRepository.readFavoriteCurrencies().flatMapLatest {
                val favorites = it.toMutableList()
                val anchorCurrency = favorites.find { it.code == anchorCurrencyCode }
                    ?: favorites.firstOrNull()
                    ?: return@flatMapLatest flow { emit(null) }

                val localRates = localRatesRepository.readAll().first()
                if (localRates.isEmpty()) refresh()

                localRatesRepository.readAll().map { rates ->
                    ExchangeRates(
                        date = Date(),
                        baseCurrency = anchorCurrency,
                        rates = rates
                    )
                }
            }
        }
    }

    override suspend fun refresh() {
        val anchorCurrencyCode = anchorCurrencyRepository.observe().first()
        val favorites = localCurrencyRepository.readFavoriteCurrencies().first()
        localRatesRepository.deleteAll()
        val anchorCurrency =
            favorites.find { it.code == anchorCurrencyCode } ?: favorites.first()

        val rates = remoteRatesRepository.getRates(
            anchorCurrency,
            favorites.toMutableList().apply { remove(anchorCurrency) })
        localRatesRepository.saveAll(rates)

        alarmService.startAlarm()
    }
}