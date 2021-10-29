package com.jaaliska.exchangerates.domain.usecases


interface FavoriteCurrenciesUseCase {
    suspend fun set(codes: List<String>)
    suspend fun get(): List<String>
}