package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.data.currency.persistence.CurrencyDao
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.usecase.UpdateCurrencyFavoriteStateUseCase

class UpdateCurrencyFavoriteStateUseCaseImpl(private val dao: CurrencyDao) :
    UpdateCurrencyFavoriteStateUseCase {

    override suspend fun invoke(currency: Currency, isFavorite: Boolean) {
        dao.setIsFavorite(code = currency.code, isFavorite = isFavorite)
    }
}