package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.domain.model.Currency
import kotlinx.coroutines.flow.Flow

interface FavoriteCurrenciesUseCase {
    suspend fun set(code: String, isFavorite: Boolean)
    fun get(): Flow<List<Currency>>
}