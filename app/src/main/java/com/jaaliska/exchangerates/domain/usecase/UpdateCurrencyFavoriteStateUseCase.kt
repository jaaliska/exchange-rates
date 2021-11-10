package com.jaaliska.exchangerates.domain.usecase

import com.jaaliska.exchangerates.domain.model.Currency

interface UpdateCurrencyFavoriteStateUseCase {
    suspend operator fun invoke(currency: Currency, isFavorite: Boolean)
}