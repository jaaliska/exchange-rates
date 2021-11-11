package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.data.rates.repository.RetrofitRatesRepository
import com.jaaliska.exchangerates.data.rates.repository.RoomRatesRepository
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.usecases.RefreshRatesUseCase
import com.jaaliska.exchangerates.presentation.service.AlarmService
import kotlinx.coroutines.*

class RefreshRatesUseCaseImpl(
    private val localRatesRepository: RoomRatesRepository,
    private val remoteRatesRepository: RetrofitRatesRepository,
    private val localCurrencyRepository: RoomCurrencyRepository,
    private val alarmService: AlarmService
) : RefreshRatesUseCase {

    override suspend operator fun invoke() {
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