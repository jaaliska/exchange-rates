package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.data.currency.dao.RoomCurrencyRepository
import com.jaaliska.exchangerates.data.rates.api.RetrofitRatesRepository
import com.jaaliska.exchangerates.data.rates.dao.RoomRatesRepository
import com.jaaliska.exchangerates.domain.usecases.RefreshRatesUseCase
import com.jaaliska.exchangerates.presentation.service.AlarmService
import com.jaaliska.exchangerates.presentation.utils.doOnError
import kotlinx.coroutines.flow.collect

class RefreshRatesUseCaseImpl(
    private val localRatesRepository: RoomRatesRepository,
    private val remoteRatesRepository: RetrofitRatesRepository,
    private val localCurrencyRepository: RoomCurrencyRepository,
    private val alarmService: AlarmService
) : RefreshRatesUseCase {

    override suspend operator fun invoke() {
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