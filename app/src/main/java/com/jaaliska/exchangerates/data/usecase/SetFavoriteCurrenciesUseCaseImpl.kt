package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.domain.usecases.RefreshRatesUseCase
import com.jaaliska.exchangerates.domain.usecases.SetFavoriteCurrenciesUseCase

class SetFavoriteCurrenciesUseCaseImpl(
    private val localCurrencyRepository: RoomCurrencyRepository,
    private val refreshRatesUseCase: RefreshRatesUseCase
) : SetFavoriteCurrenciesUseCase {

    override suspend operator fun invoke(codes: List<String>) {
        localCurrencyRepository.saveFavoriteCurrencies(codes)
        refreshRatesUseCase()
    }
}