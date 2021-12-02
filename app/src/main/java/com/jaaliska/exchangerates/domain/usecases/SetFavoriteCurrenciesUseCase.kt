package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.domain.IllegalFavoritesCountException
import com.jaaliska.exchangerates.domain.model.Currency


interface SetFavoriteCurrenciesUseCase {

    @Throws(IllegalFavoritesCountException::class)
    suspend fun invoke(codes: Set<String>)
}