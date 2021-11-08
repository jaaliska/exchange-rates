package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.data.currency.dao.RoomCurrencyRepository
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.usecases.FavoriteCurrenciesUseCase
import kotlinx.coroutines.flow.Flow

class FavoriteCurrenciesUseCaseImpl(
    private val localCurrencyRepository: RoomCurrencyRepository
) : FavoriteCurrenciesUseCase {

    override suspend fun set(code: String, isFavorite: Boolean) {
        localCurrencyRepository.markAsFavorite(currencyCode = code, isFavorite = isFavorite)
    }

    override fun get(): Flow<List<Currency>> {
        return localCurrencyRepository.readFavoriteCurrencies()
    }
}