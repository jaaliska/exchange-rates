package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.domain.model.Currency


interface FavoriteCurrenciesUseCase {
    suspend fun set(codes: Set<String>)
    suspend fun get(): List<Currency>
}