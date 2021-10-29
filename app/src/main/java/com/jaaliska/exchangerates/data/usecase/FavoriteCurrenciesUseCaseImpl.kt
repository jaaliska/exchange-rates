package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.domain.usecases.FavoriteCurrenciesUseCase
import com.jaaliska.exchangerates.domain.usecases.RefreshRatesUseCase

class FavoriteCurrenciesUseCaseImpl(
    private val localCurrencyRepository: RoomCurrencyRepository,
    private val refreshRatesUseCase: RefreshRatesUseCase,
) : FavoriteCurrenciesUseCase {

    override suspend fun set(codes: List<String>) {
        localCurrencyRepository.saveFavoriteCurrencies(codes)
        refreshRatesUseCase()
    }

    override suspend fun get(): List<String> {
        return localCurrencyRepository.readFavoriteCurrencies()
    }
}