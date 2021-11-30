package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.domain.IllegalFavoritesCountException
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository
import com.jaaliska.exchangerates.domain.usecases.FavoriteCurrenciesUseCase
import com.jaaliska.exchangerates.domain.usecases.RefreshRatesUseCase

class FavoriteCurrenciesUseCaseImpl(
    private val localCurrencyRepository: RoomCurrencyRepository,
    private val refreshRatesUseCase: RefreshRatesUseCase,
    private val preferencesRepository: PreferencesRepository
) : FavoriteCurrenciesUseCase {

    override suspend fun set(favorites: List<Currency>) {
//        val oldFavorites = localCurrencyRepository.readFavoriteCurrencies()
//        if (oldFavorites == favorites) return
        if (favorites.count() < 2) throw IllegalFavoritesCountException()
        val codes = favorites.map { it.code }

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