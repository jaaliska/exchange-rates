package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.domain.repository.FavoriteCurrenciesRepository
import com.jaaliska.exchangerates.domain.repository.RatesRepository

class UpdateRatesUseCase(
    private val localRatesRepository: RatesRepository,
    private val favoriteCurrencyRepository: FavoriteCurrenciesRepository,
    private val getRatesUseCase: GetRatesUseCase,
) {
    suspend operator fun invoke(): Int {
        val favorites = favoriteCurrencyRepository.readFavoriteCurrencies()
        localRatesRepository.deleteAllRates()
        for (baseCurrencyCode in favorites) {
            val codesToLoad = favorites.filter { it != baseCurrencyCode }
            // async
            getRatesUseCase(baseCurrencyCode, codesToLoad)
        }
        // TODO: update currency
        // TODO: schedule the timer
        return 5
    }
}