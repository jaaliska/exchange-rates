package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.data.currency.dao.RoomCurrencyRepository
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.usecase.UpdateCurrencyFavoriteStateUseCase

class UpdateCurrencyFavoriteStateUseCaseImpl(
    private val localCurrencyRepository: RoomCurrencyRepository
) : UpdateCurrencyFavoriteStateUseCase {

    override suspend fun invoke(currency: Currency, isFavorite: Boolean) {
        localCurrencyRepository.markAsFavorite(currency = currency, isFavorite = isFavorite)
    }
}