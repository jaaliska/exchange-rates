package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.domain.repository.FavoriteCurrenciesRepository

class SetFavoriteCurrenciesUseCase(
    private val favoriteCurrencyRepository: FavoriteCurrenciesRepository,
    private val updateRatesUseCase: UpdateRatesUseCase
) {
    suspend operator fun invoke(codes: List<String>) {
        favoriteCurrencyRepository.saveFavoriteCurrencies(codes)
        updateRatesUseCase()
    }
}