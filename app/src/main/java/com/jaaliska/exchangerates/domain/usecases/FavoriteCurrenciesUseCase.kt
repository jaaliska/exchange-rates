package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.domain.model.Currency


interface FavoriteCurrenciesUseCase {
    suspend fun set(favorites: List<Currency>)
    suspend fun get(): List<Currency>
}