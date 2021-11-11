package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.domain.model.Currency


interface FavoriteCurrenciesUseCase {
    suspend fun set(codes: List<String>)
    suspend fun get(): List<Currency>
}