package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository
import com.jaaliska.exchangerates.domain.usecases.FavoriteCurrenciesUseCase
import com.jaaliska.exchangerates.domain.usecases.RefreshRatesUseCase

class FavoriteCurrenciesUseCaseImpl(
    private val localCurrencyRepository: RoomCurrencyRepository,
    private val refreshRatesUseCase: RefreshRatesUseCase,
    private val preferencesRepository: PreferencesRepository
) : FavoriteCurrenciesUseCase {

    override suspend fun set(codes: Set<String>) {
        localCurrencyRepository.saveFavoriteCurrencies(codes)
        val baseCurrencyCode = preferencesRepository.getBaseCurrencyCode()
        if (!codes.contains(baseCurrencyCode)) {
            preferencesRepository.setBaseCurrencyCode(codes.first())
        }
        refreshRatesUseCase()
    }

    override suspend fun get(): List<Currency> {
        return localCurrencyRepository.readFavoriteCurrencies()
    }
}