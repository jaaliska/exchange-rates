package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.data.rates.repository.RoomRatesRepository
import com.jaaliska.exchangerates.domain.RatesNotFoundException
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.usecases.GetRatesUseCase
import com.jaaliska.exchangerates.domain.usecases.RefreshRatesUseCase

class GetRatesUseCaseImpl(
    private val localRatesRepository: RoomRatesRepository,
    private val localCurrencyRepository: RoomCurrencyRepository,
    private val refreshRatesUseCase: RefreshRatesUseCase,
) : GetRatesUseCase {

    override suspend operator fun invoke(
        baseCurrencyCode: String,
        currencyCodes: List<String>?
    ): ExchangeRates {
        val favorites = currencyCodes ?: localCurrencyRepository.readFavoriteCurrencies()
        return try {
            localRatesRepository.getRates(baseCurrencyCode, favorites)
        } catch (ex: RatesNotFoundException) {
            refreshRatesUseCase()
            return localRatesRepository.getRates(baseCurrencyCode, favorites)
        }
    }
}