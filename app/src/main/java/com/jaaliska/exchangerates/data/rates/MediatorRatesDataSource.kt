package com.jaaliska.exchangerates.data.rates

import com.jaaliska.exchangerates.data.anchor_currency.SharedPrefAnchorCurrencyRepository
import com.jaaliska.exchangerates.data.currency.dao.RoomCurrencyRepository
import com.jaaliska.exchangerates.data.rates.api.RetrofitRatesRepository
import com.jaaliska.exchangerates.data.rates.dao.RoomRatesRepository
import com.jaaliska.exchangerates.domain.datasource.RatesDataSource
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.presentation.service.AlarmService
import com.jaaliska.exchangerates.presentation.utils.doOnError
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.util.*

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class MediatorRatesDataSource(
    private val localRatesRepository: RoomRatesRepository,
    private val remoteRatesRepository: RetrofitRatesRepository,
    private val localCurrencyRepository: RoomCurrencyRepository,
    private val anchorCurrencyRepository: SharedPrefAnchorCurrencyRepository,
    private val alarmService: AlarmService
) : RatesDataSource {

    override fun observe(): Flow<ExchangeRates?> {
        return anchorCurrencyRepository.observe()
            .flatMapLatest { anchorCurrencyCode ->
                localCurrencyRepository.readFavoriteCurrencies().map {
                    val favorites = it.toMutableList()
                    val anchorCurrency =
                        favorites.find { it.code == anchorCurrencyCode } ?: favorites.firstOrNull()
                    anchorCurrency ?: return@map null
                    favorites.remove(anchorCurrency)
                    val rates = remoteRatesRepository.getRates(
                        anchorCurrency,
                        favorites.apply { remove(anchorCurrency) })

                    ExchangeRates(date = Date(), baseCurrency = anchorCurrency, rates = rates)
                }
            }
    }

    override suspend fun refresh() {
        localCurrencyRepository.readFavoriteCurrencies().doOnError { print(it) }
            .collect { favorites ->
                localRatesRepository.deleteAll()
                val anchorCurrency = favorites.first()
                val rates = remoteRatesRepository.getRates(
                    anchorCurrency,
                    favorites.toMutableList().apply { remove(anchorCurrency) })
                localRatesRepository.saveAll(rates)

                alarmService.startAlarm()
            }
    }

}